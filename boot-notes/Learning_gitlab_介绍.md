> 除了早期github/gitlab 搭配者jenkins做自动化部署，随着gitlab新版本开始默认支持K8S集群测试后，自动集成测试又有了新的内容

> 不是第一次用gitlab，但是由于版本的更新，最新的gitlab都是自动进行CI，因而遇到了一些困扰

> gitlab版本:11.11.2 for Mac

> [官方教程](https://docs.gitlab.com/ee/ci/quick_start/README.html)

### 一些概念
- 持续交付 ：频繁地将软件的新版本，交付给质量团队或者用户，以供评审
- 持续部署 ：持续交付的下一步，指的是代码通过评审以后，自动部署到生产环境

### 什么是gitlab
- 是类似于github一样的代码管理、缺陷管理的平台

### 什么是Gitlab CI
- GitLab CI 是 GitLab Continuous Integration （Gitlab 持续集成）的简称
- 从 GitLab 的 8.0 版本开始，GitLab 就全面集成了 Gitlab-CI,并且对所有项目默认开启
- 需要在项目根目录下配置只.gitlab-ci.yml 文件，并且配置了 Runner，那么每一次合并请求（MR）或者 push 都会触发 CI pipeline

### 什么是gitlab-runner
- Gitlab-runner是.gitlab-ci.yml脚本的运行器，Gitlab-runner是基于Gitlab-CI的API进行构建的相互隔离的机器（或虚拟机）
- GitLab Runner 不需要和Gitlab安装在同一台机器上，但是考虑到GitLab Runner的资源消耗问题和安全问题，也不建议这两者安装在同一台机器上。
- Gitlab Runner分为两种，Shared runners和Specific runners。
- Specific runners只能被指定的项目使用，Shared runners则可以运行所有开启 Allow shared runners选项的项目。

### 它们的关系
- Pipelines为流水线，流水线包含有多个阶段（stages），每个阶段包含有一个或多个工序（jobs）

### 什么是PipeLine
- 一次pipeline就能够出发一次构建任务
- 可以包含多个流程，如安装依赖、运行测试、编译、部署测试服务器、部署生产服务器等流程
- 任何提交或者 Merge Request 的合并都可以触发 Pipeline

### 什么是stage
- stage就是构建的阶段，在整个pipeline内部，需要全部执行成功，pipeline才算成功，需要顺序执行

### 什么是job
- job在stage的内部，可以使多个并行

### 什么是Badges
- 徽章，当Pipelines执行完成，会生成徽章，你可以将这些徽章加入到你的README.md文件或者你的网站
- pipeline|running


### 安装gitlab

### 安装gitlab-runner
- [Mac装gitlab-runner](https://docs.gitlab.com/runner/install/osx.html),我自己就用了homebrew装了，但是建议有容器还是拿容器装，Mac自身的安全会带来一些麻烦
```text
brew install gitlab-runner

brew services start gitlab-runner

这边官方也没有说查看一下状态，那又要复习一下brew的命令
[sudo] brew services list
List all running services for the current user (or root).

[sudo] brew services run (formula|--all)
Run the service formula without registering to launch at login (or boot).

[sudo] brew services start (formula|--all)
Start the service formula immediately and register it to launch at login (or
boot).

[sudo] brew services stop (formula|--all)
Stop the service formula immediately and unregister it from launching at login
(or boot).

[sudo] brew services restart (formula|--all)
Stop (if necessary) and start the service formula immediately and register it
to launch at login (or boot).

[sudo] brew services cleanup
Remove all unused services.

没有专门查看状态的命令，就只能用brew services list来查看状态了
Name          Status  User      Plist
gitlab-runner started xxx /Users/xxxx/Library/LaunchAgents/homebrew.mxcl.gitlab-runner.plist

xxxx:~ xxxxx$ brew services  stop gitlab-runner
Stopping `gitlab-runner`... (might take a while)
==> Successfully stopped `gitlab-runner` (label: homebrew.mxcl.gitlab-runner)

```

### 注册runner
- 真的很尴尬，网上的教程很乱，版本也不说明，写法也不一样
```text
xxxx:xx xxxx$ sudo gitlab-runner register
Password:
Running in system-mode.                            
                                                   
Please enter the gitlab-ci coordinator URL (e.g. https://gitlab.com/):
http://xx.xxx.xxx.xxx/
Please enter the gitlab-ci token for this runner:
xxxxxxxxxxxxx
Please enter the gitlab-ci description for this runner:
[xxx]: xxx-macbook  -- 可以稍后设置，回车会有默认值
Please enter the gitlab-ci tags for this runner (comma separated):
maven
Whether to run untagged builds [true/false]:
[false]: true -- 可以之后配置
Whether to lock Runner to current project [true/false]:
[false]: false ---可以之后配置
Registering runner... succeeded                     runner=xxxxxx
Please enter the executor: docker, virtualbox, kubernetes, docker+machine, docker-ssh+machine, docker-ssh, parallels, shell, ssh:
docker --- 我们选择用docker来部署，当然k8s是强推的
Please enter the default Docker image (e.g. ruby:2.1):
docker:stable  ---选择需要的版本
Runner registered successfully. Feel free to start it, but if it's running already the config should be automatically reloaded! 
xxxx:xxx xxxxxx$ 
```


### push .gitlab-ci.yml to GitLab
- [书写配置文件](http://www.ttlsa.com/auto/gitlab-cicd-gitlab-ci-yml-configuration-tasks-detailed/) 

### gitlab + ci +docker 进行自动部署
- 这个部署需要一定的基础，不然很难manage，需要一个项目能够通过dockerfile达成镜像，在容器中需要能够跑，其次需要了解runner和.gitlab-ci.yml文件的书写和含义，并最终能够跟着教程来做
- [服务端：centos7 + 客户端：window 全套](https://www.cnblogs.com/lufeechen/p/10405789.html)
- [官方教程](https://docs.gitlab.com/runner/install/docker.html)
- [教程](https://www.cnblogs.com/vickey-wu/p/9163475.html)
- [有一个教程](https://blog.csdn.net/Chengzi_comm/article/details/78778284)
- [踩坑记录](https://segmentfault.com/a/1190000007180257)
- [花椒直播中的实践](https://zhuanlan.zhihu.com/p/69513606)


### github/gitlab + jenkins + docker 
- 当然gitlab支持k8s部署是一个两点，但是功能上比老牌的Jenkins还是略微的弱一些，如果正式一些的CI还是用jenkins
- [参考](https://zhuanlan.zhihu.com/p/58875091)
### 删除一个项目
- 选择一个项目
- 到达项目主页
- 点击左侧setting
- 选择General
- 选择最下面的Advanced settings
- 到达最下面，选择remove project





