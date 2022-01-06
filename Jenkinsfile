#!/bin/groovy

pipeline {
    agent any
    tools { maven "M3" }

    stages {
        stage('GitHub Fetch') { steps{
            echo(message: 'GitHub Fetch!')
            git(branch: 'dev', url: 'git@github.com:Heads-in-the-Cloud/bookings-microservice-AP.git')
        }}
        stage('Tests') { steps{
            echo(message: 'Testing!')
        }}
        stage('Build') { steps{
            echo(message: 'Building!')
            sh(script: 'mvn clean package')
        }}
        stage('Archive artifacts and Deployment') { steps{
            echo(message: 'Deploying!')
            archiveArtifacts(artifacts: 'target/*.jar')
        }}
    }
}