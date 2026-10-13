pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
    }

    post {
        always {
            junit 'course-registration-app/target/surefire-reports/TEST-*.xml'
        }
        success {
            echo 'Build and Tests completed successfully.'
        }
        failure {
            echo 'Pipeline failed. Check the test results or build logs.'
        }
    }
}