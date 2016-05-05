node {
  stage 'Build'
  checkout scm
  sh 'mvn clean package'
}

parallel 'Quality scan': {
    node {sh 'mvn sonar:sonar'}
}, 'integration test': {
    node {sh 'mvn verify'}
}