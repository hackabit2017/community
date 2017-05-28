import React from 'react';

const Event = props => (
  <span className="list-group-item event-item">
    {
      <li className="">
        <h2 className="list-group-item-heading">{props.event.title}</h2>
        <p className="list-group-item-text text-left"><strong>User: </strong>{props.event.user}</p>
        <p className="list-group-item-text text-left"><strong>Created on: </strong>{props.event.date}</p>
        <p className="list-group-item-text text-left"><strong>Description: </strong>{props.event.description}</p>
        <p className="list-group-item-text text-left"><strong>latitude: </strong>{props.event.lat}</p>
        <p className="list-group-item-text text-left"><strong>longitude: </strong>{props.event.lon}</p>
      </li>
    }
  </span>
)


export default Event;


