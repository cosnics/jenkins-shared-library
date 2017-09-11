def notify(String buildStatus = 'STARTED') {
        // Build status of null means success.
        buildStatus = buildStatus ?: 'SUCCESS'

        def extraMessage = ""
        def color

        if (buildStatus == 'STARTED') {
            color = '#D4DADF'
            extraMessage = getChangeString()
        } else if (buildStatus == 'SUCCESS') {
            color = '#BDFFC3'
            extraMessage = " - <https://demo.cosnics.org/${branch}|demo>"
        } else if (buildStatus == 'UNSTABLE') {
            color = '#FFFE89'
        } else {
            color = '#FF9FA1'
        }

        def msg = "BUILD ${buildStatus}: `${env.JOB_NAME}` <${env.BUILD_URL}|#${env.BUILD_NUMBER}> ${extraMessage}"

        slackSend(color: color, message: msg)
 }

@NonCPS
def getChangeString() {
    MAX_MSG_LEN = 100
    def changeString = ""
    def changeLogSets = currentBuild.changeSets
    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            truncated_msg = entry.msg.take(MAX_MSG_LEN)
            changeString += " - ${truncated_msg} [${entry.author.getFullName()}]\n"
        }
    }

    if (!changeString) {
        changeString = " - No new changes"
    }

    return changeString
}