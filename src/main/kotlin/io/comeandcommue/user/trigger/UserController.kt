package io.comeandcommue.user.trigger

import io.comeandcommue.user.application.ChangeNicknameUseCase
import io.comeandcommue.user.application.CreateUserUseCase
import io.comeandcommue.user.application.GetUserUseCase
import io.comeandcommue.user.common.JwtTokenProvider
import io.comeandcommue.user.domain.user.UserDto
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
@RequestMapping("/user")
class UserController(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val changeNicknameUseCase: ChangeNicknameUseCase,
    private val jwtTokenProvider: JwtTokenProvider
) {
    val authCookieName = "__auth_token_"

    @PostMapping
    fun createUser(response: HttpServletResponse): ResponseEntity<UserDto> {
        val createdUserDto = createUserUseCase.createUser()

        response.setHeader(
            HttpHeaders.SET_COOKIE,
            ResponseCookie.from(authCookieName, jwtTokenProvider.createAuthToken(createdUserDto))
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofSeconds(7776000)) // 90 Days
                .sameSite("None")
                .build()
                .toString()
        )

        return ResponseEntity.ok(createdUserDto)
    }

    @GetMapping
    fun getUser(request: HttpServletRequest): ResponseEntity<UserDto> {
        val authToken = request.cookies
            ?.firstOrNull { it.name == authCookieName }
            ?.value

        // 토큰이 없으면 401
        if (authToken.isNullOrBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        return  ResponseEntity.ok(
            getUserUseCase.getUser(jwtTokenProvider.extractSubject(authToken))
        )
    }

    @PatchMapping("/nickname")
    fun updateNickname(request: HttpServletRequest): ResponseEntity<UserDto> {
        val authToken = request.cookies
            ?.firstOrNull { it.name == authCookieName }
            ?.value

        // 토큰이 없으면 401
        if (authToken.isNullOrBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        return ResponseEntity.ok(
            changeNicknameUseCase.changeNickname(jwtTokenProvider.extractSubject(authToken))
        )
    }
}
