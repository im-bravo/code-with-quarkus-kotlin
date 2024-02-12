package org.bravo.survey.entity

import org.hibernate.HibernateException
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.AbstractJavaType
import ulid.ULID

@Suppress("UNCHECKED_CAST")
class UlidJavaType : AbstractJavaType<ULID>(ULID::class.java) {
    override fun <X : Any?> unwrap(value: ULID?, type: Class<X>?, options: WrapperOptions?): X? {
        return value?.let {
            value.toBytes() as X
        }
    }

    @Throws(HibernateException::class)
    override fun <X : Any?> wrap(value: X, options: WrapperOptions?): ULID? {
        if ( value == null) {
            return null
        }
        if (ByteArray::class.isInstance(value)) {
            return ULID.fromBytes(value as ByteArray)
        }
        throw unknownWrap(value.javaClass)
    }

}