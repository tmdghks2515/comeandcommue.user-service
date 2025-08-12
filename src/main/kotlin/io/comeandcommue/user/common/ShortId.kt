package io.comeandcommue.user.common

import org.hibernate.annotations.IdGeneratorType


@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@IdGeneratorType(ShortIdGenerator::class)
annotation class ShortId
