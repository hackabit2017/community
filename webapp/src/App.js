import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Header from './components/Header'
import Wall from './components/Wall'
import { Grid, Row, Col } from 'react-bootstrap'

class App extends Component {
  render() {
    return (
      <div className="App">
        <Header />
        <div className="App-header">
          <h2>Welcome to CommUnity</h2>
        </div>
        <Grid>
          <Row >
            <Col md={8}>
            </Col>
            <Col md={4}>
              <Wall />
            </Col>
          </Row>
        </Grid>
      </div>
    );
  }
}

export default App;
