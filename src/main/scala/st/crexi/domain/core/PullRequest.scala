package st.crexi.domain.core

/**
 * 開発レポジトリに出されるプルリクのEntity
 *
 * @param id
 * @param title
 * @param body
 * @param project
 * @param destinationBranch
 */
case class PullRequest(id:Int,
                       title:String,
                       body:PullRequestBody,
                       project:Project,
                       destinationBranch:String) {


	def update(task:Option[Set[MergeTask]], reviewerTasks:Set[ReviewTask]):PullRequest = {
    copy(body = body.copy(relation = task, reviewTask = reviewerTasks))
  }
}

/**
 * プルリク自体の説明文等のvalueオブジェクト
 * @param content
 * @param checkMethod
 * @param ticketName
 * @param reviewTask
 * @param relation
 */
case class PullRequestBody(content:String,
                           checkMethod:String,
                           ticketName:String,
                           reviewTask:Set[ReviewTask],
                           relation:Option[Set[MergeTask]] = None)


/**
 * 出されたプルリクの関連プルリクで、マージされたかどうかを示すvalueオブジェクト
 *
 * @param completed
 * @param pullRequestNumber
 */
case class MergeTask(completed:Boolean, pullRequestNumber:Int) extends Task {
  override def equals(other:Any) = other match {
    case that: MergeTask => (that canEqual this) && (this.pullRequestNumber == that.pullRequestNumber)
    case _ => false
  }
}


/**
 * プルリクの完了条件にあるレビュータスクのvalueオブジェクト
 * @param completed
 * @param reviewer
 * @param unCompleted
 */
case class ReviewTask(completed: Boolean, reviewer: String, unCompleted: Set[Int] = Set.empty[Int]) extends Task {
  override def equals(other: Any) = other match {
    case that: ReviewTask => (that canEqual this) && (this.reviewer == that.reviewer)
    case _ => false

  }
}
