import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import Header from './components/Header'

class App extends Component {
  render() {
    return (
      <div className="App">
        <Header />
        <div className="App-header">
          <h2>Welcome to CommUnity</h2>
        </div>
        <p className="App-intro">
          Events coming from firebase:
        </p>
      </div>
    );
  }
}

export default App;
