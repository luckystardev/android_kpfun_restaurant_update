<?php
require_once('inc_mail.php');
require_once('inc_db_init.php');
require_once('inc_functions.php');
require_once('inc_log.php');
require_once('GCM.php');

session_start();
header( "Content-Type: application/json", true );

function createRandomID() {
 
	$chars = "abcdefghijkmnopqrstuvwxyz0123456789?";
	//srand((double) microtime() * 1000000);
	 
	$i = 0;
	 
	$pass = "";
	 
	while ($i <= 5) {
	 
		$num = rand() % 33;
		 
		$tmp = substr($chars, $num, 1);
		 
		$pass = $pass . $tmp;
		 
		$i++;
	}
	return $pass;
}

$error=false;


function check_email($email) {
    // First, we check that there's one @ symbol, and that the lengths are right
    if (!preg_match("/^[^@]{1,64}@[^@]{1,255}$/", $email)) {
        // Email invalid because wrong number of characters in one section, or wrong number of @ symbols.
        return false;
    }
    // Split it into sections to make life easier
    $email_array = explode("@", $email);
    $local_array = explode(".", $email_array[0]);
    for ($i = 0; $i < sizeof($local_array); $i++) {
        if (!preg_match("/^(([A-Za-z0-9!#$%&'*+\/=?^_`{|}~-][A-Za-z0-9!#$%&'*+\/=?^_`{|}~\.-]{0,63})|(\"[^(\\|\")]{0,62}\"))$/", $local_array[$i])) {
            return false;
        }
    }
    if (!preg_match("/^\[?[0-9\.]+\]?$/", $email_array[1])) { // Check if domain is IP. If not, it should be valid domain name
        $domain_array = explode(".", $email_array[1]);
        if (sizeof($domain_array) < 2) {
            return false; // Not enough parts to domain
        }
        for ($i = 0; $i < sizeof($domain_array); $i++) {
            if (!preg_match("/^(([A-Za-z0-9][A-Za-z0-9-]{0,61}[A-Za-z0-9])|([A-Za-z0-9]+))$/", $domain_array[$i])) {
                return false;
            }
        }
    }

    return true;
}



/***********************
	SECURITY CHECK
************************/

if(isset($_POST)){
	if($_POST['token']=='Ks9N2xIpr4'){
		
		
			
		/***********************
			REQUESTS
		************************/
		if($_POST['mode']=='login') {
	
			/***********************
				SET UN/PW
			************************/
			if(isset($_POST['server'])){
				extract($_POST['info']);	
			}else{
				$password=$_POST['password'];
				$username=$_POST['username'];
				$gcm_regid=$_POST['gcm_regid'];
			}
			/***********************
				PULL DATA FROM DB
			************************/
				
			$db = new db();
			
			$where="username='" . $username . "' AND password='" . $password . "'";
			$partners = $db->select('*', 'partners', $where);
			
			/***********************
				USE DATA
			************************/
			
			if(count($partners)!=1){$error="Invalid username/password combination.  Please try again."; }
			else{
				foreach ($partners as $partner) {
					
					$db_vars=array("gcm_regid"=>$gcm_regid);
					$where="user_id='" . $user_id . "'";
					$db->update('members', $db_vars, $where);
					
					
					$user_id=$partner['user_id'];
					
					$where="user_id='" . $partner['user_id'] . "'";
					$users = $db->select('*', $partner['table_name'], $where);
					
					
					
					foreach ($users as $user){
						
						
						
						
						$img_thumb=$user['img_thumb'];
						$first_name=$user['first_name'];
						$last_name=$user['last_name'];
						
						
						if($user['dealer_id']==""){
							/****  FREE USER ****/
							$dealer_id='0';	
						}else{
							/****  PAID USER ****/
							$dealer_id=$user['dealer_id'];
						}
						
						$email=$user['email'];
						$phone=$user['phone'];
						$zip_code=$user['zip_code'];

					}
					
				}
				
			}
				
      
			echo json_encode( array( "error" => $error, "user_id"=>$user_id, "img_thumb"=>$img_thumb, "first_name"=>$first_name, "last_name"=>$last_name, "dealer_id"=>$dealer_id, "email"=>$email, "phone"=>$phone, "zip_code"=>$zip_code) );





		
		
		
		}elseif($_POST['mode']=='register'){
		
			/**************************
				COMPLETE REGISTRATION
			**************************/
			
			/***********************
				CHECK VARIABLES PASSED
			************************/
			
			if(isset($_POST['server'])){
				extract($_POST['info']);	
			}else{
				
				$password=$_POST['password'];
				$username=$_POST['username'];
				$email=$_POST['email'];
				$phone=$_POST['phone'];
				$first_name=$_POST['first_name'];
				$last_name=$_POST['last_name']; 
				$gcm_regid=$_POST['gcm_regid'];
			}
			
			$phone=format_phone($phone);
			
			$db = new db();
			
			$where="username='" . $username . "'";
			$partners = $db->select('*', 'partners', $where);
			
			if(count($partners)>0){
				$error="Username already taken, please try again.";	
				
			}elseif(!check_email($email)){
				
				$error="Invalid email address.";
			}else{
				
				
				$db = new db();
				$id=$db->insert('members', $db_vars);
				$insertID=$id;
				
				$db_vars2 = array("user_id"=>$id, "table_name"=>'members', "username"=>$username, "password"=>$password, "title"=>'Free Member');
				$id2=$db->insert('partners', $db_vars2);
			}
			
			
	     
		   echo json_encode( array( "error" => $error) );
		}elseif($_POST['mode']=='search'){
		
			
			/***********************
				CHECK VARIABLES PASSED
			************************/
			
			if(isset($_POST['server'])){
				extract($_POST['info']);	
			}else{
				
				$latitude=$_POST['latitude'];
				$longitude=$_POST['longitude'];
				$zip_code=$_POST['zip_code'];
				$user_id=$_POST['user_id'];
			}
			
			if(strlen($latitude) > 1 && strlen($longitude)>1){
				//ALL GOOD
				
			}elseif(strlen($zip_code)==5){
				//GET LAT/LONG
				
				$where="zip_code='" . $zip_code . "'";
				$zips = $db->select('*', 'zip_codes', $where);
				
				if(count($zips)<1){
					$error="Invalid zip code, please try again.";	
					
				}else{
					foreach($zips as $zip){
						$latitude=$zip['latitude'];
						$longitude=$zip['longitude'];	
						
					}	
					
				}
			}else{
				$error="Invalid location, please try again.";
					
			}
			
			if(!$error){
				// INITIAL POINT
	
				$coords = array('latitude' => $latitude, 'longitude' => $longitude);
				
				//RADIUS
				
				$radius = 90;
			
				$begin_date= date("Y-m-01");
				$end_date= date("Y-m-01", strtotime('next month'));
			
				$db = new db();
				
				$day=date(l);
				$hour=date(G);
				
				//$select = "primary_city, zip_code, ( 3959 * acos( cos( radians( {$coords['latitude']} ) ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians( {$coords['longitude']} ) ) + sin( radians( {$coords['latitude']} ) ) * sin( radians( latitude ) ) ) ) AS distance ";
				$select = "d.id, r.company_name, r.category, d.terms, r.user_id, r.img, r.img_thumb, r.website, r.street_address, r.street_address2, r.city, r.state, r.country, r.zip_code, r.phone, primary_city, ROUND( ( 3959 * acos( cos( radians( {$coords['latitude']} ) ) * cos( radians( latitude ) ) * cos( radians( longitude ) - radians( {$coords['longitude']} ) ) + sin( radians( {$coords['latitude']} ) ) * sin( radians( latitude ) ) ) ) , 2) AS distance, d.percent,  (SELECT discount FROM discounts ds WHERE ds.id=d.id AND ds.start<='{$hour}' AND ds.end>'{$hour}' AND day='{$day}') AS discount";
				
				$from=" zip_codes LEFT JOIN restaurants r on zip_codes.`zip_code`=r.`zip_code` LEFT JOIN deals d ON r.user_id=d.`user_id`";
				
				$where="  d.`status`='approved' HAVING distance <= {$radius} ORDER BY distance";
				
				$restaurants = $db->simple_select($select, $from, $where);
				
				foreach($restaurants as $restaurant){
					if(is_numeric($restaurant['discount'])){
						$restaurant['percent']==$restaurant['discount'];	
					}
					
				}
	
				
				
				
			}
			
			
	     
		   echo json_encode( array( "error" => $error, "restaurants"=>$restaurants) );
		}elseif($_POST['mode']=='bonus'){
		
			
			
			if(isset($_POST['server'])){
				extract($_POST['info']);	
			}else{
				
				$user_id=$_POST['user_id'];
			}
			
			$db = new db();
			
			$where="user_id='" . $user_id . "' AND status='approved'";
			$bonuses = $db->select('*', 'bonus', $where);
			
			if(count($bonuses)==0){
				$error=true;	
				
			}else{
				
				foreach($bonuses as $bonus){
					$id=$bonus['id'];
					$title=$bonus['title'];
					$terms=$bonus['terms'];
					$deal_img=$bonus['deal_img'];
					$deal_img_thumb=$bonus['deal_img_thumb'];	
					
					
				}
			}
			
			
	     
		   echo json_encode( array( "error" => $error, "id"=>$id, "title"=>$title, "terms"=>$terms, "deal_img"=>$deal_img, "deal_img_thumb"=>$deal_img_thumb) );
		}elseif($_POST['mode']=='getFriends'){
		
			if(isset($_POST['server'])){
				extract($_POST['info']);	
			}else{
				
				
				$user_id=$_POST['user_id'];
			}

			
			/************************
				Fiends should be passed as a multidimensional array.
				
				[contacts]array{
					[0]array{
						first_name
						last_name
						phone
						}
					[1]array{
						first_name
						last_name
						phone
						}
					[2]array{
						first_name
						last_name
						phone
						}
				}
			************************/			
			$contacts=$_POST['contacts'];
			
			/*For Testing
			$member1=array('first_name'=>'Joey', 'last_name'=>'Paid', 'phone'=>'8885953775');
			$member2=array('first_name'=>'Son', 'last_name'=>'Iam', 'phone'=>'097-912-6710');
			$member3=array('first_name'=>'Chi', 'last_name'=>'DuongVan', 'phone'=>'0977337873');
			
			$contacts=array($member1, $member2, $member3);
			var_dump($contacts);
			*/
			
			$phone_numbers=array();
			
			foreach($contacts as $contact){
				array_push($phone_numbers, format_phone($contact['phone']));
				
			}
			

			$db = new db();
			/*$from= "friends f left join members m on f.friend_id=m.user_id left join partners p on m.user_id=p.user_id";
			$where="f.user_id='" . $_POST['user_id'] . "' AND p.table_name='members'";
			
			$friends = $db->select('p.username, m.*', 'deals', $where);
			*/
			$where="user_id='" . $user_id . "'";
			
			/*************************
				Get list of friends user_id's and convert to array
			*************************/
			$friends = $db->select('friend_id', 'friends', $where);
			
			
			$friends_list=array();
			foreach($friends as $friend){
				array_push($friends_list, $friend['friend_id']);	
				
			}
			$friends=$friends_list;
			
			/************************
				PULL ALL MEMBERS
			**************************/
			$members=$db->select('user_id, first_name, last_name, phone', 'members', '');
			
			/************************
				CHECK MEMBERS AGAINST CONTACTS FROM PHONE
			**************************/
			$registered_members=array();
			foreach($members as $member){
				if(in_array(format_phone($member['phone']), $phone_numbers)){
						array_push($registered_members, $member);
					
				}
				
				
			}
			
			/************************
				CHECK IF CONTACTS ARE ALREADY FRIENDS OR NOT  RETURNS 0 IF NOT A FRIEND, 1 IF ALREADY A FRIEND
			**************************/
			foreach($registered_members as $member){
				if(in_array($friends, $member['user_id'])){
					$member['friends']='1';	
				}	
				
			}
			
	     
		   echo json_encode( array( "error" => $error, "friends"=>$registered_members) );
		}elseif($_POST['mode']=='addFriend'){
		
			
			
			/***********************
				CHECK VARIABLES PASSED
			************************/
			
			if(isset($_POST['server'])){
				extract($_POST['info']);	
			}else{
				
				$user_id=$_POST['user_id'];
				$friend_id=$_POST['friend_id'];
			}
			
			
			
			$db = new db();
			
			$where="user_id='" . $user_id . "' AND friend_id='" . $friend_id . "'";
			$friends = $db->select('*', 'friends', $where);
			
			if(count($friends)==0){
				$db_vars = array("user_id"=>$user_id, "friend_id"=>$friend_id);
				
				$db = new db();
				$id=$db->insert('friends', $db_vars);
				
			}
			
			
	     
		   echo json_encode( array( "error" => $error) );
		}elseif($_POST['mode']=='removeFriend'){
		
		
			/***********************
				CHECK VARIABLES PASSED
			************************/
			
			if(isset($_POST['server'])){
				extract($_POST['info']);	
			}else{
				
				$user_id=$_POST['user_id'];
				$friend_id=$_POST['friend_id'];
			}
			
			
			
			$db = new db();
			
			$where="user_id='" . $user_id . "' AND friend_id='" . $friend_id . "'";
			$friends = $db->select('*', 'friends', $where);
			
			
			if(count($friends)>0){
				$db = new db();
				$db->delete("friends", $where);
				
			}
			
			
	     
		   echo json_encode( array( "error" => $error) );
		}elseif($_POST['mode']=='createVoucher'){
		
			
			
			/***************************
				CHECK VARIABLES PASSED
			****************************/
			
			if(isset($_POST['server'])){
				extract($_POST['info']);	
			}else{
				
				$user_id=$_POST['user_id'];
				$deal_id=$_POST['deal_id']; /* id */
				//$user_img=$_POST['user_img'];
				$user_id_company=$_POST['user_id_company'];
				$caption=$_POST['caption'];
				
				
			}
			
			
			/***************************
				UPLOAD USER IMAGE
			****************************/
			$base = $_REQUEST["image"];
 
			if (isset($base)) {
		 
				$suffix = createRandomID();
				$image_name = "img_".$suffix."_".date("Y-m-d-H-m-s").".jpg";
				 
				// base64 encoded utf-8 string
				$binary = base64_decode($base);
				 
				// binary, utf-8 bytes
				 
				header("Content-Type: bitmap; charset=utf-8");
				 
				$file = fopen("/uploads/" . $image_name, "wb");
				 
				fwrite($file, $binary);
				 
				fclose($file);
				 
				die($image_name);
		 
			}else{
				$image_name='none.jpg';	
			}
			
			$user_img='/uploads/'.$image_name;
			
			$today=date("Y-m-d");
			
			
			/*************************
				SELECT FRIENDS
			*************************/
			
			$db = new db();
			$where="user_id='".$user_id."'";
			$friends=$db->simple_select('friends.*, m.gcm_regid', 'friends left join members m on friends.friend_id=m.user_id', $where);
			
			
			$user=$db->simple_select('CONCAT(first_name, " ", last_name) AS name', 'members', $where);
			$first_key = key($user);
			$user=$user[$first_key];
			$user_name=$user['name'];
			//var_dump($friends);
			
			//GET DEAL
			$where="status='approved' AND user_id='".$user_id_company."'";
			$deals=$db->select('*','deals', $where);
			$first_key = key($deals);
		
			$deal=$deals[$first_key];
			
			//Calculate Percentage
			$percent='10';
			
			
			//Insert Details
			$db_vars_details=array("bonus_id"=>$deal_id, "deal_id"=>$deal['id'], "created_by"=>$user_id, "user_img"=>$user_img, "caption"=>$caption, "create_date"=>$today,  "percent"=>$percent);
			$id=$db->insert('reward_details', $db_vars_details);
			
			
			$registration_ids = array();
			/*************************
				CREATE SAMPLES
			*************************/
			foreach($friends as $friend){
				$voucher=create_voucher('rewards');
				$db_vars = array("user_id"=>$friend['friend_id'], "reward_id"=>$id, "voucher"=>$voucher);
				$db->insert('rewards', $db_vars);
				
				array_push($registration_ids, $friend['gcm_regid']);
				
				
			}
			
			/*************************
				SEND PUSH NOTIFICATION
			*************************/
			$message = array('bonus', '{$user_name} has sent you a Gift!');
		    $gcm = new GCM();
		 
		    $result = $gcm->send_notification($registatoin_ids, $message);
			 
			    
			
	     	//Insert Voucher
	     	$voucher=create_voucher('rewards');
			$db_vars = array("user_id"=>$user_id, "reward_id"=>$id, "voucher"=>$voucher);
			$db->insert('rewards', $db_vars);
			
			
				
		   echo json_encode( array( "error" => $error, "voucher"=>$voucher, "percent"=>$percent) );
		}elseif($_POST['mode']=='getGifts'){
		
			
			/***********************
				CHECK VARIABLES PASSED
			************************/
			
			if(isset($_POST['server'])){
				extract($_POST['info']);	
			}else{
				
				$user_id=$_POST['user_id'];
			}
			
			
			
				
			
				$db = new db();
				
				$select = "r.voucher, DATE_ADD(rd.create_date, INTERVAL 7 DAY) AS expiration, rd.user_img, rd.caption, d.title, d.terms, d.deal_img, rest.company_name, rest.street_address, rest.street_address2, rest.city, rest.state, rest.country, rest.zip_code";
				
				$from=" rewards r LEFT JOIN reward_details rd on r.reward_id=rd.id LEFT JOIN bonus d on d.id=rd.deal_id LEFT JOIN restaurants rest on rest.user_id=d.user_id";
				
				$where="r.user_id='{$user_id}' AND r.claimed='no'";
				
				$rewards = $db->simple_select($select, $from, $where);
	
				
				if(count($rewards)==0){
					$error="No Gifts Found.";
					
				}
				
				
				
			
			
			
	     
		   echo json_encode( array( "error" => $error, "rewards"=>$rewards) );
		}elseif($_POST['mode']=='getRewards'){
		
			
			/***********************
				CHECK VARIABLES PASSED
			************************/
			
			if(isset($_POST['server'])){
				extract($_POST['info']);	
			}else{
				
				$user_id=$_POST['user_id'];
			}
			
			
			
				
			
				$db = new db();
				
				$select = "d.title, r.voucher, rd.percent, DATE_ADD(rd.create_date, INTERVAL 3 DAY) AS expiration, rd.user_img, rd.caption, d.terms, rest.img, rest.company_name, rest.street_address, rest.street_address2, rest.city, rest.state, rest.country, rest.zip_code";
				
				$from=" rewards r LEFT JOIN reward_details rd on r.reward_id=rd.id LEFT JOIN deals d on d.id=rd.deal_id LEFT JOIN restaurants rest on rest.user_id=d.user_id";
				
				$where="r.user_id='{$user_id}' AND rd.percent > 0 AND r.claimed='no'";
				
				$rewards = $db->simple_select($select, $from, $where);
	
				
				if(count($rewards)==0){
					$error="No Rewards found. Create one from the search menu.";	
					
				}
				
				
				
			
			
			
	     
		   echo json_encode( array( "error" => $error, "rewards"=>$rewards) );
		}elseif($_POST['mode']=='table'){
		
			
			
			
			$db = new db();
			
			$results = $db->select('*', $_POST['table'], '');
			
			//var_dump($deals);
			
			if(count($results)==0){
				$table="Table is empty.";	
				
			}else{
				$first_key = key($results);
				$titles=$results[$first_key];
				
				$table="<table><thead><tr>";
				foreach($titles as $key=>$value){
					$table.="<th>{$key}</th>";
					
				}
				$table.="</tr></thead><tbody>";
				
				
				foreach($results as $result){
					$table.="<tr>";
						foreach($result as $key=>$value){
							$table.="<td>{$value}</td>";	
							
						}
					$table.="</tr>";
					
					
				}
				$table.="</tbody></table>";
			}
			
			
	     
		   echo json_encode( array( "error" => $error, "table"=>$table) );
		}else{
			echo json_encode( array( "error" => 'Invalid Method.') );
		}
	
	}else{
		echo json_encode( array( "error" => 'Invalid token.') );
	}	
	
	
}else{
	echo json_encode( array( "error" => 'Invalid page request.') );
	
}	
	
	



?>