import React from 'react';
import { getEvents } from '../actions/events';
import { connect } from 'react-redux';

const Event = props => (
  <span className="list-group-item event-item">
    {
      <li className="">
        <h2 className="list-group-item-heading">{props.event.title}</h2>
        <p className="list-group-item-text"><strong>Created on: </strong>{props.event.date}</p>
        <p className="list-group-item-text"><strong>Description: </strong>{props.event.description}</p>
        <p className="list-group-item-text"><strong>photo: </strong>{props.event.photoUrl}</p>
        <p className="list-group-item-text"><strong>latitude: </strong>{props.event.lat}</p>
        <p className="list-group-item-text"><strong>longitude: </strong>{props.event.lon}</p>
      </li>
    }
  </span>
)


export default Event;


