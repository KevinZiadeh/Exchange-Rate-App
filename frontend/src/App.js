import './App.css';
import React, { useCallback, useState, useEffect } from "react";
import { AppBar, Toolbar, Button, Typography, Alert, FormControl, Select, MenuItem, Snackbar, Switch, FormControlLabel, FormGroup, TextField, InputLabel, } from "@mui/material";
import UserCredentialsDialog from "./UserCredentialsDialog/UserCredentialsDialog";
import { getUserToken, saveUserToken, clearUserToken } from "./localStorage";
import ArrowDownwardTwoToneIcon from '@mui/icons-material/ArrowDownwardTwoTone';
import ArrowUpwardTwoToneIcon from '@mui/icons-material/ArrowUpwardTwoTone';
import { DataGrid } from '@mui/x-data-grid';
import CanvasJS from "canvasjs";



var SERVER_URL = "http://127.0.0.1:5000"



function App() {




  const States = {
    PENDING: "PENDING",
    USER_CREATION: "USER_CREATION",
    USER_LOG_IN: "USER_LOG_IN",
    USER_AUTHENTICATED: "USER_AUTHENTICATED",
  };


  let [buyUsdRate, setBuyUsdRate] = useState(null);
  let [sellUsdRate, setSellUsdRate] = useState(null);
  let [lbpInput, setLbpInput] = useState("");
  let [usdInput, setUsdInput] = useState("");
  let [transactionType, setTransactionType] = useState("usd-to-lbp");
  let [lbpInputExchange, setLbpInputExchange] = useState("");
  let [usdInputExchange, setUsdInputExchange] = useState("");
  let [receiverUsername, setReceiverUsername] = useState("");
  let [transactionTypeExchange, setTransactionTypeExchange] = useState("usd-to-lbp");
  let [userToken, setUserToken] = useState(getUserToken());
  let [authState, setAuthState] = useState(States.PENDING);
  let [usdConvertChecked, setUsdConvertChecked] = React.useState(true);
  let [usdConvertAmount, setUsdConvertAmount] = React.useState("");
  let [lbpConvertAmount, setLbpConvertAmount] = React.useState("");
  let [conversionError, setConversionError] = React.useState(false);
  let [transactionError, setTransactionError] = React.useState(false);
  let [userTransactions, setUserTransactions] = useState([]);
  let [userGiven, setUserGiven] = useState([]);
  let [userReceived, setUserReceived] = useState([]);
  let [StatRows, setStatRows] = useState([])
  let [ChartLBPTOUSD, setChartLBPTOUSD] = useState([]);
  let [ChartUSDTOLBP, setChartUSDTOLBP] = useState([]);


  function login(username, password) {
    return fetch(`${SERVER_URL}/authentication`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        user_name: username,
        password: password,
      }),
    })
      .then((response) => response.json())
      .then((body) => {
        setAuthState(States.USER_AUTHENTICATED);
        setUserToken(body.token);
        saveUserToken(body.token);
      });
  }

  function logout() {
    setAuthState(null);
    setUserToken(null);
    clearUserToken();
  }
  function createUser(username, password) {
    return fetch(`${SERVER_URL}/user`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        user_name: username,
        password: password,
      }),
    }).then((response) => login(username, password));
  }

  function fetchStats() {
    fetch(`${SERVER_URL}/statistics`)
      .then(response => response.json())
      .then(stats => {
        const rows = [];
        var i = 0;
        for (var key in stats) {
          var label = key.split("_");
          console.log(label)
          for (let i = 0; i < label.length; i++) {
            label[i] = label[i][0].toUpperCase() + label[i].substring(1);
          }
          label = label.join(" ");
          rows.push({ id: i, name: label, value: String(stats[key]) + " LBP" })
          i = i + 1;
        }
        setStatRows(rows)
      });
  }

  useEffect(fetchStats, []);

  function fetchCharts() {
    fetch(`${SERVER_URL}/graph`)
      .then(response => response.json())
      .then(charts => {
        const chartLBPTOUSD = []
        const chartUSDTOLBP = []
        const dates = charts["date"];
        const mean_rate_lbp_to_usd = charts["mean_rate_lbp_to_usd"];
        const mean_rate_usd_to_lbp = charts["mean_rate_usd_to_lbp"];
        for (let i = 0; i < dates.length; i++){
          const temp_date = dates[i]
          temp_date = temp_date.split()
           chartLBPTOUSD.push({x: temp_date , y:mean_rate_lbp_to_usd[i]});
           chartUSDTOLBP.push({x: temp_date , y:mean_rate_usd_to_lbp[i]});
        }
        setChartLBPTOUSD(chartLBPTOUSD);
        setChartUSDTOLBP(chartUSDTOLBP); 
      });
  }


  useEffect(fetchCharts, []);

  function fetchRates() {
    fetch(`${SERVER_URL}/exchangeRate`)
      .then(response => response.json())
      .then(data => {
        console.log(data)
        if (data["usd_to_lbp"] === "N.A") {
          setSellUsdRate(data["usd_to_lbp"]);
        }
        else {
          setSellUsdRate(data["usd_to_lbp"].toFixed(2));
        }
        if (data["lbp_to_usd"] === "N.A") {
          setBuyUsdRate(data["lbp_to_usd"]);
        }
        else {
          setBuyUsdRate(data["lbp_to_usd"].toFixed(2));
        }
      });
  }

  useEffect(fetchRates, []);

  function convert() {
    if (usdConvertChecked) {
      if (!sellUsdRate || sellUsdRate === "Not available yet" || !usdConvertAmount) {
        setConversionError(true);
        return null;
      }
      let value = parseFloat(usdConvertAmount) * parseFloat(sellUsdRate);
      setLbpConvertAmount(value.toString());
    } else {
      if (!buyUsdRate || buyUsdRate === "Not available yet" || !lbpConvertAmount) {
        setConversionError(true);
        return null;
      }
      let value = parseFloat(lbpConvertAmount) / parseFloat(buyUsdRate);
      setUsdConvertAmount(value.toString());
    }
    return null;
  }
  function addItem() {
    if (usdInput === "" || lbpInput === "" || usdInput === "0" || lbpInput === "0" || !transactionType) {
      setTransactionError(true);
      return;
    }

    let body = {
      "usd_amount": usdInput,
      "lbp_amount": lbpInput,
      "usd_to_lbp": transactionType === "usd-to-lbp"

    }

    let header = {
      'Content-Type': 'application/json',
    }
    if (userToken !== null) {
      header.Authorization = 'Bearer ' + userToken
    }

    fetch(`${SERVER_URL}/transaction`,
      {
        method: 'POST',
        headers: header,
        body: JSON.stringify(body)
      })
      .then(() => fetchRates())
      .then(() => {
        if (userToken) {
          fetchUserTransactions()
        }
      });
    setUsdInput("");
    setLbpInput("");
  }


  function addExchange() {
    if (usdInputExchange === "" || lbpInputExchange === "" || usdInputExchange === "0" || lbpInputExchange === "0" || !transactionType) {
      setTransactionError(true);
      return;
    }

    let body = {
      "usd_amount": usdInputExchange,
      "lbp_amount": lbpInputExchange,
      "receiver_name": receiverUsername,
      "usd_to_lbp": transactionType === "usd-to-lbp"

    }

    console.log(receiverUsername)
    let header = {
      'Content-Type': 'application/json',
    }
    if (userToken !== null) {
      header.Authorization = 'Bearer ' + userToken
    }

    fetch(`${SERVER_URL}/exchangeuser`,
      {
        method: 'POST',
        headers: header,
        body: JSON.stringify(body)
      })
      .then(() => fetchRates())
      .then(() => {
        if (userToken) {
          fetchUserTransactions()
        }
      });
    setUsdInputExchange("");
    setLbpInputExchange("");
    setReceiverUsername("");
  }


  const fetchUserTransactions = useCallback(() => {
    fetch(`${SERVER_URL}/transaction`, {
      headers: {
        Authorization: `bearer ${userToken}`,
      },
    })
      .then((response) => response.json())
      .then((transactions) => setUserTransactions(transactions))
  }, [userToken]);

  const fetchUserExchanges = useCallback(() => {
    fetch(`${SERVER_URL}/exchangeuser`, {
      headers: {
        Authorization: `bearer ${userToken}`,
      },
    })
      .then((response) => response.json())
      .then((exchanges) => {
        setUserReceived(exchanges["receive"])
        setUserGiven(exchanges["give"])
      })
  }, [userToken]);


  useEffect(() => {
    if (userToken) {
      fetchUserExchanges();
    }
  }, [fetchUserExchanges, userToken]);



  useEffect(() => {
    if (userToken) {
      fetchUserTransactions();
    }
  }, [fetchUserTransactions, userToken]);


  return (
    <div>
      <UserCredentialsDialog open={authState === States.USER_CREATION}
        title={"Register"}
        submitText={"Register"}
        onClose={() => setAuthState(States.PENDING)}
        onSubmit={createUser}
      />
      <UserCredentialsDialog open={authState === States.USER_LOG_IN}
        title={"Login"}
        submitText={"Submit"}
        onClose={() => setAuthState(States.PENDING)}
        onSubmit={login}
      />

      <AppBar position="static">
        <Toolbar classes={{ root: "nav" }} >
          <Typography variant="h5" sx={{ flexGrow: 1 }} >Lebanese Pounds (LBP) to USD Exchange Tracker</Typography>
          <div>
            {userToken !== null ? (
              <Button color="inherit" onClick={logout}>Logout</Button>) :
              (
                <div>
                  <Button color="inherit" onClick={() => setAuthState(States.USER_CREATION)}>Register</Button>
                  <Button color="inherit" onClick={() => setAuthState(States.USER_LOG_IN)}>Login</Button>
                </div>
              )}
          </div>

        </Toolbar>
      </AppBar>

      <div className="wrapper">
        <h2>Today's Exchange Rate</h2>
        <p>LBP to USD Exchange Rate</p>
        <h3>Buy USD: <span id="buy-usd-rate">
          {buyUsdRate === "N.A" ? buyUsdRate : buyUsdRate}
        </span></h3>
        <h3>Sell USD: <span id="sell-usd-rate">
          {console.log(typeof (sellUsdRate))}
          {sellUsdRate === "N.A" ? sellUsdRate : sellUsdRate}
        </span></h3>

        <hr />
        <Typography variant="h5" sx={{ m: 1 }}>Rate Calculator</Typography>
        <FormGroup style={{ width: '80%', margin: 'auto' }}>
          <TextField
            type="number"
            label="USD Amount"
            value={usdConvertAmount}
            onChange={({ target: { value } }) => setUsdConvertAmount(value)}
            inputProps={{ min: 0, style: { textAlign: 'center' } }}
            size="small"

          />
          {usdConvertChecked ? (
            <ArrowDownwardTwoToneIcon sx={{ margin: "10px auto" }} />
          ) : (
            <ArrowUpwardTwoToneIcon sx={{ margin: "10px auto" }} />
          )}
          <TextField
            type="number"
            label="LBP Amount"
            value={lbpConvertAmount}
            onChange={({ target: { value } }) => setLbpConvertAmount(value)}
            inputProps={{ min: 0, style: { textAlign: 'center' } }}
            size="small"
          />
          <FormControlLabel
            control={<Switch onChange={() => setUsdConvertChecked(!usdConvertChecked)} defaultChecked />}
            label="USD to LBP"
            style={{ margin: 'auto' }}
          />
          <Button
            color="primary"
            variant="contained"
            size="small"
            onClick={convert}
          >
            Convert
          </Button>
        </FormGroup>


      </div>
      <div className="wrapper">
        <FormGroup style={{ width: '50%', margin: 'auto' }}>
          <Typography variant="h5" align="center">Record a recent transaction</Typography>
          <FormGroup>
            <TextField
              sx={{ m: 1 }}
              type="number"
              label="USD Amount"
              value={usdInput}
              onChange={e => setUsdInput(e.target.value)}
              fullWidth
            />

            <TextField
              sx={{ m: 1 }}
              type="number"
              label="LBP Amount"
              value={lbpInput}
              onChange={e => setLbpInput(e.target.value)}
              fullWidth
            />

            <FormControl>
              <InputLabel sx={{ m: 1 }}>Transaction Direction</InputLabel>
              <Select
                sx={{ m: 1 }}
                labelId="select-label"
                value={transactionType}
                label="Transaction Direction"
                onChange={e => setTransactionType(e.target.value)}
              >
                <MenuItem value="usd-to-lbp">USD to LBP</MenuItem>
                <MenuItem value="lbp-to-usd">LBP to USD</MenuItem>
              </Select>
            </FormControl>

            <Button type="button" color="primary" variant="contained"
              onClick={addItem}>Add</Button>
          </FormGroup>
        </FormGroup>



      </div>



      <Snackbar
        elevation={6}
        variant="filled"
        open={authState === States.USER_AUTHENTICATED}
        autoHideDuration={2000}
        onClose={() => setAuthState(States.PENDING)}
      >
        <Alert severity="success">Success</Alert>
      </Snackbar>
      {userToken && (
        <div>


          <div className="wrapper">
            <FormGroup style={{ width: '50%', margin: 'auto' }}>
              <Typography variant="h5" align="center">Record an exchange with a user</Typography>
              <FormGroup>
                <TextField
                  sx={{ m: 1 }}
                  type="number"
                  label="USD Amount"
                  value={usdInputExchange}
                  onChange={e => setUsdInputExchange(e.target.value)}
                  fullWidth
                />

                <TextField
                  sx={{ m: 1 }}
                  type="number"
                  label="LBP Amount"
                  value={lbpInputExchange}
                  onChange={e => setLbpInputExchange(e.target.value)}
                  fullWidth
                />

                <TextField
                  sx={{ m: 1 }}
                  type="string"
                  label="Receiver Username"
                  value={receiverUsername}
                  onChange={e => setReceiverUsername(e.target.value)}
                  fullWidth
                />

                <FormControl>
                  <InputLabel sx={{ m: 1 }}>Transaction Direction</InputLabel>
                  <Select
                    sx={{ m: 1 }}
                    labelId="select-label"
                    value={transactionTypeExchange}
                    label="Transaction Direction"
                    onChange={e => setTransactionTypeExchange(e.target.value)}
                  >
                    <MenuItem value="usd-to-lbp">USD to LBP</MenuItem>
                    <MenuItem value="lbp-to-usd">LBP to USD</MenuItem>
                  </Select>
                </FormControl>

                <Button type="button" color="primary" variant="contained"
                  onClick={addExchange}>Add</Button>
              </FormGroup>
            </FormGroup>



          </div>


          <div className="wrapper">
            <Typography variant="h5">Your Transactions</Typography>
            <DataGrid
              rows={userTransactions}
              columns={[
                { field: "usd_amount", headerName: 'USD Amount', type: 'number', flex: 0.3 },
                { field: "lbp_amount", headerName: 'LBP Amount', type: 'number', flex: 0.3 },
                { field: "usd_to_lbp", headerName: 'USD to LBP conversion', type: 'boolean', flex: 0.4 },
                { field: "added_date", headerName: 'Date Added', type: 'dateTime', flex: 0.6 }
              ]}
              autoHeight
            />
          </div>
          <div className="wrapper">
            <Typography variant="h5">Given Transactions</Typography>
            <DataGrid
              rows={userGiven}
              columns={[
                { field: "receiver_name", headerName: 'Receiver Name', type: 'string', flex: 0.3 },
                { field: "usd_amount", headerName: 'USD Amount', type: 'number', flex: 0.3 },
                { field: "lbp_amount", headerName: 'LBP Amount', type: 'number', flex: 0.3 },
                { field: "usd_to_lbp", headerName: 'USD to LBP conversion', type: 'boolean', flex: 0.4 },
                { field: "added_date", headerName: 'Date Added', type: 'dateTime', flex: 0.6 }
              ]}
              autoHeight
            />
          </div>
          <div className="wrapper">
            <Typography variant="h5">Received Transactions</Typography>
            <DataGrid
              rows={userReceived}
              columns={[
                { field: "user_name", headerName: 'User Name', type: 'string', flex: 0.3 },
                { field: "usd_amount", headerName: 'USD Amount', type: 'number', flex: 0.3 },
                { field: "lbp_amount", headerName: 'LBP Amount', type: 'number', flex: 0.3 },
                { field: "usd_to_lbp", headerName: 'USD to LBP conversion', type: 'boolean', flex: 0.4 },
                { field: "added_date", headerName: 'Date Added', type: 'dateTime', flex: 0.6 }
              ]}
              autoHeight
            />
          </div>

        </div>
      )}

      <div className="wrapper">
      <Typography variant="h5">Statistics</Typography>
        <DataGrid
          rows={StatRows}
          columns={[
            { field: "name", headerName: 'Stat', type: 'string', flex: 0.3 },
            { field: "value", headerName: 'Value', type: 'number', flex: 0.3 }
          ]}
          autoHeight
        />
      </div>
    </div>
  );
}

export default App;
