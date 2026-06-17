pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'Ex4',
                    url: 'https://github.com/aYukine/final-devops-exam.git'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew build -x test'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }

        stage('Deploy with Ansible') {
            steps {
                sh 'ansible-playbook -i inventory.ini playbook.yml'
            }
        }
    }

    post {
        failure {
            script {
                def gitEmail = sh(script: 'git log -1 --pretty=format:%ae', returnStdout: true).trim()
                mail to: "srengty@gmail.com, ${gitEmail}",
                    subject: "❌ Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        Build failed!
                        Job: ${env.JOB_NAME}
                        Build: #${env.BUILD_NUMBER}
                        Commit: ${env.GIT_COMMIT}
                        Check: ${env.BUILD_URL}
                    """
            }
        }
    }
}