package io.comeandcommue.user.common

import org.hibernate.generator.BeforeExecutionGenerator
import org.hibernate.generator.EventType
import org.hibernate.engine.spi.SharedSessionContractImplementor
import java.io.Serializable
import java.util.*
import java.util.Base64

class ShortIdGenerator : BeforeExecutionGenerator {

    override fun getEventTypes(): EnumSet<EventType> =
        EnumSet.of(EventType.INSERT)

    override fun generate(
        session: SharedSessionContractImplementor?,
        owner: Any?,
        currentValue: Any?,
        eventType: EventType?
    ): Serializable {
        // 이미 값이 있으면 그대로 사용
        if (currentValue != null) {
            return currentValue as Serializable
        }

        val uuid = UUID.randomUUID()
        val bytes = uuid.toByteArray()

        // URL-safe Base64, padding 제거 → 앞 11자 사용
        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes)
            .substring(0, 11)
    }

    private fun UUID.toByteArray(): ByteArray {
        val bytes = ByteArray(16)
        val most = this.mostSignificantBits
        val least = this.leastSignificantBits

        for (i in 0 until 8) {
            bytes[i] = ((most ushr (8 * (7 - i))) and 0xFF).toByte()
        }
        for (i in 0 until 8) {
            bytes[8 + i] = ((least ushr (8 * (7 - i))) and 0xFF).toByte()
        }
        return bytes
    }
}
