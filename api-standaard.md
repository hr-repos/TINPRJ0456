# API standard

**DeleteProject:**
- **method:** `POST`
- **URL:** `"api/delete-project"`
- **Request:** 
    ```json
    {
        "id": int
    }
    ```
- **Response:** `null`

------------------------------------------------------------------------------------------------------
</br>

**ExportToCsv:**
- **method:** `GET`
- **URL:** `"api/export-to-csv/{project_id}"`
- **Request:** `[urlParam] "project_id": int`
- **Response:** the sensor data in a CSV string

------------------------------------------------------------------------------------------------------
</br>

**GetFrequency:**
- **method:** `GET`
- **URL:** `"api/get-frequency"`
- **Request:** `null`
- **Response:** 
    ```json
    {
        "frequency": int
    }
    ```
------------------------------------------------------------------------------------------------------
</br>

**GetProject:**
- **method:** `GET`
- **URL:** `"api/get-project/{id}"` | `"api/get-active-project"`
- **Request:** `[urlParam] "id": int` | `null`
- **Response:** 
    ```json
    {
        "frequency": int
    }
    ```
------------------------------------------------------------------------------------------------------
</br>

**GetProject:**
- **method:** `GET`
- **URL:** `"api/get-project/{id}"` | `"api/get-active-project"`
- **Request:** `[urlParam] "id": int` | `null`
- **Response:** 
    ```json
    {
        "sensors": 
        [{
            "id": int,
            "name": string,
            "pin": int,
            "unit": string,
            "calibrate": bool
        }],
        "pins":
        [{
            "pin": int,
            "a": float,
            "b": float,
            "c": float
        }]
    }
    ```
------------------------------------------------------------------------------------------------------
</br>

**GetProjects:**
- **method:** `GET`
- **URL:** `"api/get-projects"`
- **Request:** `null`
- **Response:** 
    ```json
    {
        "id": int,
        "name": string,
        "creation_date": long,
        "description": string,
        "creator_name": string,
        "frequency": int,
        "active": bool,
        "sensors": 
        [{
            "id": int,
            "name": string,
            "pin": int,
            "unit": string,
            "calibrate": bool
        }]
    }
    ```
------------------------------------------------------------------------------------------------------
</br>

**GetSensors:**
- **method:** `GET`
- **URL:** `"api/get-sensors/{project_id}"`
- **Request:** `[urlParam] "project_id": int` 
- **Response:** 
    ```json
    {
        "sensors": 
        [{
            "id": int,
            "name": string,
            "pin": int,
            "unit": string,
            "calibrate": bool
        }],
        "project":
        {
            "id": int,
            "name": string,
            "creation_date": long,
            "description": string,
            "creator_name": string,
            "frequency": int,
            "active": bool,
        }
    }
    ```
------------------------------------------------------------------------------------------------------
</br>

**Ping:**
- **method:** `GET`
- **URL:** `"api/ping"`
- **Request:** `null` 
- **Response:** 
    ```json
    {
        "message": "Pong!"
    }
    ```

**Ping:**
- **method:** `POST`
- **URL:** `"api/ping"`
- **Request:** `any` (body gets printed in server)
- **Response:** 
    ```json
    {
        "message": "Pong!"
    }
    ```
------------------------------------------------------------------------------------------------------
</br>

**SubmitCalibration:**
- **method:** `POST`
- **URL:** `"api/submit-calibration"`
- **Request:** 
    ```json
    {
        "sensorId": int,
        "a": float,
        "b": float,
        "c": float
    }
    ```
- **Response:**
    ```json
    {
        "success": true
    }
    ```
------------------------------------------------------------------------------------------------------
</br>

**SubmitCalibration:**
- **method:** `POST`
- **URL:** `"api/submit-calibration"`
- **Request:** 
    ```json
    {
        "sensorId": int,
        "a": float,
        "b": float,
        "c": float
    }
    ```
- **Response:**
    ```json
    {
        "success": true
    }
   ```
------------------------------------------------------------------------------------------------------
</br>

**SubmitProject:**
- **method:** `POST`
- **URL:** `"api/submit-project"`
- **Request:** 
    ```json
    {
        "name": string,
        "description": string,
        "creatorName": string,
        "active": bool,
        "frequency": int
    }
    ```
- **Response:**
    ```json
    {
        "id": int,
        "name": string,
        "creation_date": long,
        "description": string,
        "creator_name": string,
        "frequency": int,
        "active": bool,
        "sensors": 
        [{
            "id": int,
            "name": string,
            "pin": int,
            "unit": string,
            "calibrate": bool
        }]
    }
    ```
------------------------------------------------------------------------------------------------------
</br>

**SubmitProjectActiveChange:**
- **method:** `POST`
- **URL:** `"api/submit-project-active-change"`
- **Request:** `[rawBody]: int` (project_id)
- **Response:**
    ```json
    {
        "updated": int
    }
    ```
------------------------------------------------------------------------------------------------------
</br>

**SubmitSensor:**
- **method:** `POST`
- **URL:** `"api/submit-sensor"`
- **Request:** 
    ```json
    {
        "name": string,
        "'pin'": int,
        "project": int,
        "unit": string
    }
    ```
- **Response:**
    ```json
    {
        "id": int
    }
    ```
------------------------------------------------------------------------------------------------------
</br>

**SubmitSensorData:**
- **method:** `POST`
- **URL:** `"api/sensors"` | `"api/submit-sensor-data"`
- **Request:** 
    ```json
    {
        "data": Array<int>
    }
    ```
- **Response:** `null`
- **websocket broadcast:**
    ```json
    {
        "sensor_data": Array<int>
    }
    ```