<?php

    require("config.php");

    if (!empty($_POST)) {

        $response = array(
            "error" => FALSE
        );

        $query = "SELECT * FROM `watchUsers_table` WHERE `uniqueId` = :uniqueId";

        //now lets update what :user should be
        $query_params = array(
            ':uniqueId' => $_POST['uniqueId']
        );

        try {
            $stmt = $db->prepare($query);
            $result = $stmt->execute($query_params);
        }

        catch (PDOException $ex) {

            $response["error"] = TRUE;
            $response["message"] = $ex->getMessage();
            die(json_encode($response));
        }

        $row = $stmt->fetch();

        if ($row) {

            $response["error"] = TRUE;
            $response["message"] = "I'm sorry, this uniqueId is already in use";
            die(json_encode($response));

        } else {

            $query = "INSERT INTO watchUsers_table ( `uniqueId`, `nameWatchUser`, `birthDate`, `userId_fk`, `familyLink` ) 
            VALUES ( :uniqueId, :nameWatchUser, :birthDate, :userId_fk, :familyLink )";


            $query_params = array(
                ':uniqueId' => $_POST['uniqueId'],
                ':nameWatchUser' => $_POST['nameWatchUser'],
                ':birthDate' => $_POST['birthDate'],
                ':userId_fk' => $_POST['userId_fk'],
                ':familyLink' => $_POST['familyLink']
            );

            try {
                $stmt = $db->prepare($query);
                $result = $stmt->execute($query_params);
            }

            catch (PDOException $ex) {
                $response["error"] = TRUE;
                $response["message"] = $ex->getMessage();
                die(json_encode($response));
            }      

            $response["error"] = FALSE;
            $response["message"] = "Register successful!";
            $response["lastInsertId"] = $db->lastInsertId();
            echo json_encode($response);			
		
        }

    } else {
        echo json_encode(array("message" => "Method not supported!"));
    }
