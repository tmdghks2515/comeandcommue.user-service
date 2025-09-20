package io.comeandcommue.user

import io.comeandcommue.lib.data.auditor.EnableAuditorAware
import io.comeandcommue.lib.web.exception.EnableGlobalExceptionHandling
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableJpaAuditing
@EnableGlobalExceptionHandling
@EnableAuditorAware
@ComponentScan(
	basePackages = ["io.comeandcommue.user", "io.comeandcommue.lib.web", "io.comeandcommue.lib.data"]
)
class UserServiceApplication

fun main(args: Array<String>) {
	runApplication<UserServiceApplication>(*args)
}
