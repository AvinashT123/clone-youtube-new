pipeline {
    agent any

    tools {
        // Use Maven installed and configured in Jenkins Global Tool Configuration
        maven 'Maven 3.9.9' // Replace with your configured Maven version name
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

}
