### 什么是Git  什么是版本控制
- [参看官方文档](https://git-scm.com/book/zh/v2/%E8%B5%B7%E6%AD%A5-%E5%85%B3%E4%BA%8E%E7%89%88%E6%9C%AC%E6%8E%A7%E5%88%B6)
### 基础概念
- Workspace：工作区
- Index / Stage：暂存区
- Repository：仓库区（或本地仓库）
- Remote：远程仓库
```text
                                 |------------------checkout----------------|
                                 |                                         \|/
 remote ----fetch/clone--->  repository <--commit--- index <----add---- workspace
   |    <----push--------                                                  /|\
   |------------------------------pull--------------------------------------|
```

### 新建代码库
- git init 在当前目录新建一个git代码库
- git init project- name 当前目录，新建一个目录，初始化为git代码库
- git clone url 下载一个项目

### 配置
- .gitignore文件,可以在用户主目录下，也可以在项目目录下
- git config --list 显示当前git的配置
- git config -e [--global] 编辑config 文件
- git config [--global] user.name "[name]"
- git config [--global] user.email "[email address]"

### 增加删除文件
- git add 添加到暂存区 index
- git add [dir] 把某一目录下的修改提交到暂存区
- git add . 把当前目录所有的修改提交到暂存区
- git add -p 需要每一个变化都要求确认
- git rm [file1] 把file1文件从工作区删除，并且会放入暂存区
- git rm --cached [file] 停止追踪文件，就是文件的变化不再感知，但是文件还是属于工作区中的，并不会被删除
- git mv [original] [renamed]  发明文件，并将这个改名放入暂存区

### 代码提交 
- git commit -m [comment] 把文件从暂存区提交到仓库区
- git commit [file1] [file2] ..... -m [comment] 提交指定文件 从暂存区到仓库区
- git commit -a 将所有中间变化，从工作区直接到仓库区
- git commit -v  提交时显示diff
- git commit --amend -m [message] 使用最新一次commit的代码，替换上一次的，如果代码没有变化，则改写comment
- git commit --amend [file1] [file2] ... 重做上一次commit，包括指定文件的变化

### 分支
- git branch 列出本地所有分支
- git branch -r 列出远程所有分支
- git branch -a 列出本地和远程所有分支
- git branch [name] 新建一个分支，停留在当前分支
- git checkout -b [branch] 新建一个分支，并切换到该分支
- git checkout 切换到指定分支，并更新工作区
- git checkout - 切换到上一个分支
- git cherry-pick [commit] 选择一个commit 合并到当前分支
- git branch -d [name] 删除分支
- git push origin --delete [name] 删除远程分支
- git  branch -dr [remote] 删除远程分支
- git push orgin develop 本地的develop分支推送到远程
- git branch --set-upstream-to=origin/develop develop 本地分支和远程分支建立跟踪关系

### 标签
- git tag 列出所有标签
- git tag [tag] 新建一个tag在当前commit
- git tag [tag] [commit] 新建一个tag在指定commit上
- git tag -d [tag] 删除本地tag
- git push origin :refs/tags/[name] 删除远程tag
- git show [tag] 查看tag信息
- git push [remote] [tag] 提交指定tag
- git push [remote]  --tags 提交所有tag
- git checkout -b [branch] [tag] 新建一个分支，指向某一个tag

### 查看信息
- git status 显示有变更的文件
- git log 显示历史版本
- git log --stat 显示commit 历史，每次commit发生变更的文件
- git log -S [keyword] 搜索提交历史，根据关键词
- git log [tag] HEAD --pretty=format:%s 显示某个commit之后的所有变化，每一个commit 占一行
- git log --follow [file]  / git whatchanged [file] 显示一个文件的历史版本
- git log -p [file] 显示与指定文件相关的每一个diff
- git log -5 --pretty --online 显示过去5次提交
- git shortlog -sn 显示所有提交过的用户，按提交次数排序
- git blame [file]显示指定文件在什么时候被什么人修改过
- git diff暂存区与工作区代码的差异
- git diff HEAD 显示工作区与当前分支最新commit之间的差异
- git diff --cached [file]  显示暂存区和上一个commit的差异
- git diff [first-branch]...[second-branch]  显示两次提交之间的差异
- git diff --shortstat "@{0 day ago}"  显示今天你写了多少行代码
- git show [commit]  显示某次提交的元数据和内容变化
- git show --name-only [commit] 显示某次提交发生变化的文件
- git show [commit]:[filename]  显示某次提交时，某个文件的内容
- git reflog  显示当前分支的最近几次提交 
- git rebase [branch]  从本地master拉取代码更新当前分支：branch 一般为master


### 远程同步
- git remote update 更新远程仓储
- git fetch [remote] 下载远程仓库的所有变动
- git remote -v 显示所有远程仓库
- git remote show [remote] 显示某个远程仓库的信息
- git remote add [shortname] [url] 增加一个新的远程仓库，并命名
- git pull [remote] [branch] 取回远程仓库的变化，并与本地分支合并
- git push [remote] [branch] 上传本地指定分支到远程仓库
- git push [remote] --force 强行推送当前分支到远程仓库，即使有冲突
- git push [remote] --all  推送所有分支到远程仓库

### 撤销
- git checkout [file] 恢复暂存区的指定文件到工作区
- git checkout [commit] [file] 恢复某个commit的指定文件到暂存区和工作区
- git checkout .  恢复暂存区的所有文件到工作区
- git reset [file]  重置暂存区的指定文件，与上一次commit保持一致，但工作区不变
- git reset --hard  重置暂存区与工作区，与上一次commit保持一致
- git reset [commit] 重置当前分支的指针为指定commit，同时重置暂存区，但工作区不变
- git reset --hard [commit]  重置当前分支的HEAD为指定commit，同时重置暂存区和工作区，与指定commit一致
- git reset --keep [commit]  重置当前HEAD为指定commit，但保持暂存区和工作区不变
- git revert [commit]  新建一个commit，用来撤销指定commit 、后者的所有变化都将被前者抵消，并且应用到当前分支
- git stash  暂时将未提交的变化移除
-git stash pop  稍后再移入

### 其他
- git archive 生成一个可供发布的压缩包

### git rebase 合并commit请求
- 根据基线合并，就是需要找到一个commit，以他为准，后面的几个一起提交，git rebase -i [startpoint] [endpoint]































