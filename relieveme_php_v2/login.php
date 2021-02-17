<?php

require("config.php");

if (!empty($_POST)) {

    $response = array("error" => FALSE);

    $query = "SELECT *, COALESCE((SELECT watchUsers_table.idWatchUser     FROM watchUsers_table where users.id = watchUsers_table.userId_fk  ),0) watchUsers FROM `users`  WHERE email = :email";

    $query_params = array(
        ':email' => $_POST['email']
    );

    try {
        $stmt = $db->prepare($query);
        $result = $stmt->execute($query_params);
    }

    catch (PDOException $ex) {
        $response["error"] = true;
        $response["message"] = $ex->getMessage();
        die(json_encode($response));
    }

    $validated_info = false;
    $login_ok = false;
    $email = $_POST['email'];

    $row = $stmt->fetch();

    if (password_verify($_POST['password'], $row['encrypted_password'])) {
        $login_ok = true;
    }

    if ($login_ok == true) {
        $response["error"] = false;
        $response["message"] = "Login successful!";
        $response["user"]["uid"] = $row["id"];
        $response["user"]["name"] = $row["name"];
        $response["user"]["email"] = $row["email"];
        $response["user"]["verified"] = $row["verified"];
        $response["user"]["created_at"] = $row["created_at"];
		$response["user"]["watchUsers"] = $row["watchUsers"];
        die(json_encode($response));

    } else {
        $response["error"] = true;
        $response["message"] = "Invalid Credentials!";
        die(json_encode($response));
    }

} else {
    echo json_encode(array("message" => "Method not supported!"));
}

?>
