package org.bravo.survey.entity

import java.util.EnumSet
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.generator.BeforeExecutionGenerator
import org.hibernate.generator.EventType
import org.hibernate.generator.EventTypeSets
import ulid.ULID

class UlidIdentifierGenerator : BeforeExecutionGenerator {
    override fun getEventTypes(): EnumSet<EventType> {
        return EventTypeSets.INSERT_ONLY
    }

    override fun generate(
        session: SharedSessionContractImplementor?,
        owner: Any?,
        currentValue: Any?,
        eventType: EventType?
    ): Any {
        return ULID.nextULID()
    }
}