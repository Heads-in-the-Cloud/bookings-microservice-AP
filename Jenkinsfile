#!/bin/groovy

pipeline {
    agent any
    tools { maven "M3" }

    environment {
        AWS = credentials('AWS-Key')

        def aws_script = "aws secretsmanager get-secret-value --secret-id prod/Angel/Secrets --region us-east-2"
        def output = sh(returnStdout: true, script: aws_script)
        def repos = readJSON(text: readJSON(text: output).SecretString)

        bookings_repo = repos["AP-Bookings-Repo"].toString()
    }

    stages {
        stage('GitHub Fetch') { steps{
            echo(message: 'GitHub Fetch!')
            git(branch: 'dev', url: 'https://github.com/Heads-in-the-Cloud/bookings-microservice-AP.git')
        }}
        stage('Tests') { steps{
            echo(message: 'Testing!')
        }}
        stage('Build') { steps{
            echo(message: 'Building!')
            sh(script: 'mvn clean package')
            script { image = docker.build("ap-bookings:latest") }
        }}
        stage('Archive artifacts and Deployment') { steps{
            echo(message: 'Deploying!')
            archiveArtifacts(artifacts: 'target/*.jar')

            script{
            docker.withRegistry("https://" + bookings_repo, "ecr:us-east-2:AWS-Key") {
                docker.image("ap-bookings:latest").push()
            }}
        }}
    }
}