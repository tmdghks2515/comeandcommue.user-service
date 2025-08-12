package io.comeandcommue.user.common

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.comeandcommue.user.domain.user.UserDto
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtTokenProvider {
    private val algorithm: Algorithm = Algorithm.HMAC256("f8I7Tn4Wb8zL9d3Pq6Vr1sFz8Xr2Ek3Gh1m9Yc6Lq3XnPt0a")

    fun createAuthToken(user: UserDto): String {
        return generateToken(
                subject = user.id,
                nickname = user.nickname,
        )
    }

    private fun generateToken(
        subject: String,
        nickname: String,
    ): String {
        val now = Date()
        val expiry = Date(now.time + 7776000 * 1000L)

        val builder = JWT.create()
                .withSubject(subject)
                .withClaim("nickname", nickname)
                .withIssuedAt(now)
                .withExpiresAt(expiry)

        return builder.sign(algorithm)
    }

    fun extractSubject(token: String): String {
        val verifier = JWT.require(algorithm)
            .build()

        val decoded = verifier.verify(token) // 서명/만료 검증
        return decoded.subject
    }
}
