package st.crexi.domain.core

sealed abstract class PullRequestEventType

object Closed extends PullRequestEventType

object Opened extends PullRequestEventType

object Other extends PullRequestEventType