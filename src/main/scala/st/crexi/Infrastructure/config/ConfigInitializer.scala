package st.crexi.Infrastructure.config

import com.typesafe.config.{Config, ConfigFactory}
import st.crexi.Infrastructure.github.Token

object ConfigInitializer {

	var config:Option[Config] = None

	def apply(confFile:String) = {

		val conf = config.getOrElse{
			val conf = ConfigFactory.load(confFile)
			config = Option(conf)
			config.get
		}
		InfraConfig.setup(conf)
	}
}

object InfraConfig {

	var config:Option[Config] = None

	private[config] def setup(config:Config) = {
		this.config = Option(config)
	}

	lazy val githubToken = Token(config.get.getString("github.api-token"))
}
