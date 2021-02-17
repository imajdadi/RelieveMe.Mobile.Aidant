<?php

    require("config.php");

    if (!empty($_POST)) {

        $response = array(
            "error" => FALSE
        );

     

            $query = "INSERT INTO tasks_table (  `taskDate`, `taskDescription`, `typeTask`, `taskRepetition`, `endTime`, `idUser_fk`,`idWatch_fk`, `taskState` ) 
            VALUES (:taskDate, :taskDescription, :typeTask, :taskRepetition, :endTime, :idUser_fk, :idWatch_fk, 0 )";

		
            $query_params = array(
                ':taskDate' => $_POST['taskDate'],
				':taskDescription' => $_POST['taskDescription'],
				':typeTask' => $_POST['typeTask'],
				':taskRepetition' => $_POST['taskRepetition'],
				':endTime' => $_POST['endTime'],
				':idUser_fk' => $_POST['idUser_fk'],
				':idWatch_fk' => $_POST['idWatch_fk']
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


            // Uncomment this line if you are using online server.
            //mail($email,$subject,$message,$headers);

            $response["error"] = FALSE;
            $response["message"] = "Register successful!";
            echo json_encode($response);
        

    } else {
        echo json_encode(array("message" => "Method not supported!"));
    }
