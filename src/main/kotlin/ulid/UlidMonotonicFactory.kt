package ulid

import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.ConcurrentHashMap

@ApplicationScoped
class UlidMonotonicFactory {
    private var previousULID = ConcurrentHashMap<String, ULID>()

    init {
        previousULID["default"] = ULID.nextULID()
    }

    fun nextULID(timestamp: Long = System.currentTimeMillis(), key: String = "default"): ULID {
        val previous = previousULID.getOrPut(key) { ULID.nextULID() }
        val ulid = ULID.Monotonic.nextULID(previous, timestamp)
        previousULID[key] = ulid
        return ulid
    }

}