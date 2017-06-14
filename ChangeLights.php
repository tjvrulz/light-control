<?php 
	$r = $_POST['r'];
	$g = $_POST['g'];
	$b = $_POST['b'];
	$instant = $_POST['instant'];
	
	$oldfile = fopen("oldvals.txt","r");
	fscanf($oldfile,"%d:%d:%d",$or,$og,$ob);
	fclose($oldfile);
	
	$ret = 0;
	if($instant==0)
		system("C:\setled.exe $r $g $b $or $og $ob",$ret);
	else
		system("C:\setled_old.exe $r $g $b");
	
	$file = fopen("oldvals.txt","w+");
	fwrite($file,"$r:$g:$b");
	fclose($file);
	
	echo $ret;
?>