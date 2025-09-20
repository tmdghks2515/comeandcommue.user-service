package io.comeandcommue.user.infrastructure.config

import io.comeandcommue.lib.web.loginUser.LoginUserArgumentResolver
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@RequiredArgsConstructor
class WebConfig(
    private val resolver: LoginUserArgumentResolver
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(resolver)
    }
}
