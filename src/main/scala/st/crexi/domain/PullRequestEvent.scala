package st.crexi.domain

case class PullRequestEvent(action:PullRequestEventType, pullRequest:PullRequest)