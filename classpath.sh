ls lib | xargs -I {} echo "\${lib.dir}/{}:" | xargs echo | sed 's/:$//'; echo ''
