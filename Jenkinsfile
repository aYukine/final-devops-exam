pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    environment {
        DEVELOPER_EMAIL = "${env.GIT_AUTHOR_EMAIL}"
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
            mail to: "srengty@gmail.com, ${env.GIT_AUTHOR_EMAIL}",
                subject: "❌ Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                    Build failed!
                    
                    Job: ${env.JOB_NAME}
                    Build: #${env.BUILD_NUMBER}
                    Commit: ${env.GIT_COMMIT}
                    Author: ${env.GIT_AUTHOR_EMAIL}
                    
                    Check details at: ${env.BUILD_URL}
                """
        }
        success {
            echo 'Build, test and deployment successful!'
        }
    }
}