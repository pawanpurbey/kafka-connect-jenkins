package org.aravind.oss.kafka.connector

import org.apache.kafka.connect.source.SourceTaskContext
import spock.lang.Shared
import spock.lang.Specification

import static com.github.dreamhead.moco.Moco.pathResource
import static com.github.dreamhead.moco.MocoJsonRunner.jsonHttpServer
import static com.github.dreamhead.moco.Runner.runner

/**
 * @author Aravind R Yarram
 * @since <<add version>>
 */
class JenkinsSourceTaskTest extends Specification {
    @Shared
    SourceTaskContext taskContext = Mock()

    @Shared
    def mock

    JenkinsSourceTask sourceTask

    def setupSpec() {
        def server = jsonHttpServer(8181, pathResource("JenkinsSourceTaskTest-mock-server-cfg.json"))
        mock = runner(server)
        mock.start()
    }

    def cleanupSpec() {
        mock.stop()
    }

    def setup() {
        sourceTask = new JenkinsSourceTask()
        sourceTask.initialize(taskContext)
    }

    def cleanup() {
        if (sourceTask != null) sourceTask.stop()
    }

    def "Should support single job url as taskProps"() {
        given:
        def taskProps = ['job.urls': 'http://localhost:8181/job/Abdera-trunk/']
        sourceTask.start(taskProps)

        when:
        def sourceRecord = sourceTask.poll()

        then:
        sourceRecord == null
    }

    def "Should support multiple comma separated job urls as taskProps"() {
        given:
        def taskProps = ['job.urls': 'http://localhost:8181/job/Abdera-trunk/,https://builds.apache.org/job/Accumulo-1.8/']
        sourceTask.start(taskProps)

        when:
        def sourceRecord = sourceTask.poll()

        then:
        sourceRecord == null
    }
}