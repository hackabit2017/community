import React from 'react';
import logo from './logo.svg';
import './App.css';
import { Grid, Row, Col } from 'react-bootstrap'
import ReactMapboxGl, { Layer, Feature } from "react-mapbox-gl";

import Header from './components/Header'
import Wall from './components/Wall'


const App = () => {

  return (
    <div className="App">
      <Header />
      <div className="App-header">
        <h2>Welcome to CommUnity</h2>
      </div>
      <Grid>
        <Row className="text-left">
          <Col md={8}>
            <ReactMapboxGl
              style="mapbox://styles/mapbox/streets-v9"
              accessToken="pk.eyJ1Ijoidm1hZGluY2VhIiwiYSI6ImNqMzd5NDllcjAwMnoyd285bHJodjF5emYifQ.aogNUPgaPnhLZB91IERbBw"
              containerStyle={{
                height: "100vh",
              }}>
              <Layer
                type="symbol"
                id="marker"
                layout={{ "icon-image": "marker-15" }}>
                <Feature coordinates={[-0.481747846041145, 51.3233379650232]}/>
              </Layer>
            </ReactMapboxGl>
          </Col>
          <Col md={4}>
            <Wall />
          </Col>
        </Row>
      </Grid>
    </div>
  );
}


export default App;
