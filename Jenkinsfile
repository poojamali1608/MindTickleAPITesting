pipeline {
    agent any
    tools {
            maven 'Maven 3.8.6'
            jdk 'Java 11.0.18'
        }
    stages {
        stage('---clean---') {
            steps {
                bat "mvn clean"
            }
        }
        stage('--test--') {
            steps {
                bat "mvn test"
            }
        }
        stage('--package--') {
            steps {
                bat "mvn package"
            }
        }
    }
}
