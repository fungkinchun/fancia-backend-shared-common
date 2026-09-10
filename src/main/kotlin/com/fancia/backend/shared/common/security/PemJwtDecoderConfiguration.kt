package com.fancia.backend.shared.common.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.util.StringUtils
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

@Configuration
@ConditionalOnClass(NimbusJwtDecoder::class)
@ConditionalOnMissingClass(
    "org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration",
)
@ConditionalOnExpression(
    "T(org.springframework.util.StringUtils).hasText('\${JWT_SIGNING_PUBLIC_KEY_PEM:}')",
)
@ConditionalOnProperty(
    prefix = "spring.security.oauth2.resourceserver.jwt",
    name = ["issuer-uri"],
)
class PemJwtDecoderConfiguration(
    @Value("\${JWT_SIGNING_PUBLIC_KEY_PEM}")
    private val publicKeyPem: String,
    @Value("\${JWT_SIGNING_KEY_ID:}")
    private val keyId: String,
    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private val issuerUri: String,
) {
    @Bean
    @ConditionalOnMissingBean(JwtDecoder::class)
    fun jwtDecoder(): JwtDecoder {
        val publicKey = parsePublicKeyPem(normalizePem(publicKeyPem))
        val decoder = NimbusJwtDecoder.withPublicKey(publicKey).build()
        val validators =
            buildList {
                add(JwtValidators.createDefaultWithIssuer(issuerUri))
                if (StringUtils.hasText(keyId)) {
                    add(JwtKidValidator(keyId.trim()))
                }
            }
        decoder.setJwtValidator(DelegatingOAuth2TokenValidator(validators))
        return decoder
    }

    companion object {
        private const val PUBLIC_KEY_HEADER = "BEGIN PUBLIC KEY"

        internal fun normalizePem(value: String): String =
            value.trim()
                .removeSurrounding("\"")
                .replace("\\n", "\n")
                .replace("\r\n", "\n")
                .trim()

        internal fun parsePublicKeyPem(pem: String): RSAPublicKey {
            require(pem.contains(PUBLIC_KEY_HEADER)) {
                "JWT_SIGNING_PUBLIC_KEY_PEM must be SPKI PEM (-----BEGIN PUBLIC KEY-----)"
            }
            return try {
                val body =
                    pem.lines()
                        .filter { line ->
                            !line.startsWith("-----BEGIN") && !line.startsWith("-----END")
                        }
                        .joinToString("")
                val spec = X509EncodedKeySpec(Base64.getDecoder().decode(body))
                KeyFactory.getInstance("RSA").generatePublic(spec) as RSAPublicKey
            } catch (e: Exception) {
                throw IllegalStateException("Failed to parse JWT_SIGNING_PUBLIC_KEY_PEM", e)
            }
        }
    }
}

internal class JwtKidValidator(
    private val expectedKid: String,
) : OAuth2TokenValidator<Jwt> {
    override fun validate(token: Jwt): OAuth2TokenValidatorResult {
        val kid = token.headers["kid"] as? String
        if (kid != null && kid != expectedKid) {
            return OAuth2TokenValidatorResult.failure(
                OAuth2Error(
                    "invalid_token",
                    "JWT kid does not match configured signing key",
                    null,
                ),
            )
        }
        return OAuth2TokenValidatorResult.success()
    }
}
