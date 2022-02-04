import groovy.json.JsonSlurper
import hudson.model.*
/**
 * This Script takes the Configuration from jenkins/RegisteredJobs.json and generate automatically all jobs for one repository.
 * add your Jenkins-Job to RegisteredJobs.json
 * projectName = for example "fnol-fe"
 * jobName = name of the Jenkins job. For example "javaStaging"
 * repoUrl = Url of the repo.
 * branch = branch that will apply. For example "master"
 * buildTrigger type can be the following:
 *  - false = no trigger needed (boolean)
 *  - github = trigger via githubPush (String)
 *  - build run after other projects are built. Name the project (String)
 *  - pr = trigger via pull request (String)
 * specType can be the following:
 *  - false = no special type (boolean)
 *  - removepr make job to enable remove-merge-pr feature (String)
 */

def jsonSlurper = new JsonSlurper()
//def envVars = Jenkins.instanceOrNull.getGlobalNodeProperties()[0].getEnvVars()

def jobToAutomate = jsonSlurper.parseText(readFileFromWorkspace("./jobs/RegisteredJobs.json"))
String gitTokenCredentials = "git-token-credentials"
String refSpec = "+refs/heads/*:refs/remotes/origin/* +refs/pull/*:refs/remotes/origin/pr/*"
String sha = '${sha1}'
def envRepoUrl = "${GIT_REPO_URL}"
def envRepoBranch = "${GIT_REPO_BRANCH}"


for (def job in jobToAutomate) {
  folder(job.folder)
  pipelineJob("${job.folder}/${job.jobName}") {
    def jobRepoUrl = job.repoUrl?: envRepoUrl
    def jobBranch = job.branch?: envRepoBranch
    def jenkinsFile = job.jenkinsfile

    keepDependencies(false)

    logRotator() {
      numToKeep(3)
    }

    if("${job.specType}" == 'removepr'){
      parameters {
        stringParam('PROG_PREFIX', "${job.project}-")
        stringParam('payload', "")
      }
    }
    else{
      parameters {
        stringParam('PROJECT', "${job.project}")
      }
    }


    //add trigger to project depends on buildTrigger type
    if(job.buildTrigger){
      switch ("${job.buildTrigger}") {
        case 'github':
          triggers {
            githubPush()
          }
          break
        case 'pr':
          properties {
            githubProjectUrl("${jobRepoUrl.substring(0, jobRepoUrl.length() -4)}/")
          }
          triggers {
            githubPullRequest {
              useGitHubHooks()
              permitAll()
              triggers {
                whiteListTargetBranches(['master'])
              }
            }
          }
          break
        case 'github/daily':
          triggers {
            githubPush()
            cron('@daily')
          }
          break
        case "${job.buildTrigger}":
          triggers  {
            upstream("${job.buildTrigger}", 'SUCCESS')
          }
          break
      }
    }
    if ("${job.buildTrigger}"=="pr") {
      concurrentBuild(true)
      definition {
        cpsScm {
          scm {
            git {
              remote {
                url("${jobRepoUrl}")
                credentials(gitTokenCredentials)
                refspec(refSpec)
              }
              branches(sha)
              scriptPath("${jenkinsFile}")
              extensions {}  // required as otherwise it may try to tag the repo, which you may not want
            }
          }
        }
      }
    }
    else {
      concurrentBuild(false)
      definition {
        cpsScm {
          lightweight(true)
          scm {
            git {
              remote {
                url("${jobRepoUrl}")
                credentials(gitTokenCredentials)
              }
              branches("*/${jobBranch}")
              scriptPath("${jenkinsFile}")
              extensions {}  // required as otherwise it may try to tag the repo, which you may not want
            }
          }
        }
      }
    }

    //initial trigger build process after creating the jobs.
    // running the "github" jobs once, cause the trigger works only after running manual once
    if  (job.buildTrigger in ["github"]) {
      queue("${job.jobName}")
    }
  }
}
