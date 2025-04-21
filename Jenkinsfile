pipeline {
    agent any

    tools {
        // Use Maven installed and configured in Jenkins Global Tool Configuration
        maven 'Maven 3.8.6' // Replace with your configured Maven version name
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/AvinashT123/clone-youtube-new.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml' // Publish JUnit test results
        }

        success {
            echo '✅ Build and tests passed!'
        }

        failure {
            echo '❌ Build or tests failed!'
        }
    }
}
