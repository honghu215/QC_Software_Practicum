# Tips for programming languages, softwares, etc  

## Git command  
* List existing remotes  

```
git remote -v
```  

* change remote's URL  

	```bash
	git remote set-url origin https://github.com/USERNAME/REPOSITORY.git
	```  
* force to push to remote repository  

	```bash
	git push -u origin master -f
	``` 
  
* Remove the old remote  

	```
	git remote remove myOrigin
	```  
* Add missing remote  
	
	```
	git remote add origin ssh://github.com/USERNAME/REPOSITORY.git
	```  
	
* Delete branch
	
	1. local
		
		```bash
		git branch -d <branch_name>
		```
	2. remote
		
		```bash
		git push --delete <remote_name> <branch_name>
		```

* fetch remote branch to local

	```bash
	git checkout --track -b local_branch_name origin/remote_branch_name
	``` 
