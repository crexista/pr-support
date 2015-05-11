package st.crexi.domain.core

case class PullRequestEvent(action:PullRequestEventType, pullRequest:PullRequest)