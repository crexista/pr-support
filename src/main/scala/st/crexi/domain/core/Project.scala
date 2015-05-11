package st.crexi.domain.core

/**
 * githubにおける開発レポジトリ(プロジェクト)をしめすEntityです
 *
 * @param id
 * @param name
 * @param owner
 * @param defaultBranch
 * @param baseURL
 */
case class Project(id:Int, name:String, owner:String, defaultBranch:String, baseURL:String)