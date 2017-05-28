import React from 'react';
import logo from './Community_logo_6.png';
import './App.css';
import { Grid, Row, Col } from 'react-bootstrap'

import Header from './components/Header'
import Wall from './components/Wall'
import EventsMap from './components/EventsMap'


const App = () => {

  return (
    <div className="App">
      <Header />
      <div className="App-header">
          <img src={logo} className="App-logo"></img>
          <h2 className="App_title">CommUnity</h2>
      </div>
      <Grid>
        <Row className="text-left">
          <Col md={8}>
            <EventsMap/>
          </Col>
          <Col md={4} className="events_list">
            <Wall />
          </Col>
        </Row>
      </Grid>
    </div>
  );
}


export default App;
