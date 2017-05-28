import React from 'react';
import { Image } from 'react';
import '../App.css';

const Event = props => (
  <span id="item" className=" list-group-item event-item">
    <div className ="col-md-6">
      <img src={props.event.photoUrl} className="event_img"></img>
    </div>
    {
      <div className="col-md-6">
        <h2 className="list-group-item-heading">{props.event.title}</h2>
        <p className="list-group-item-text text-left"><strong>User: </strong>{props.event.user}</p>
        <p className="list-group-item-text text-left"><strong>Created on: </strong>{props.event.date}</p>
        <p className="list-group-item-text text-left"><strong>Description: </strong>{props.event.description}</p>
        <p className="list-group-item-text text-left"><strong>Category: </strong>{props.event.tag}</p>
        <p className="list-group-item-text text-left"><strong>Rating: </strong>{props.event.rating}</p>
        <p className="list-group-item-text text-left"><strong>Position: </strong>
          {props.event.lat}, {props.event.lon}
        </p>
      </div>
    }
  </span>
)

// <Col md={6}>
//     <img src={props.event.photoUrl} height="42" width="60"></img>
//     </Col>
//     <Col md={6}>
//     {
// <li className="">
//   <h2 className="list-group-item-heading">{props.event.title}</h2>
//   <p className="list-group-item-text text-left"><strong>User: </strong>{props.event.user}</p>
//   <p className="list-group-item-text text-left"><strong>Created on: </strong>{props.event.date}</p>
//   <p className="list-group-item-text text-left"><strong>Description: </strong>{props.event.description}</p>
//   <p className="list-group-item-text text-left"><strong>Position: </strong>
//       {props.event.lat}, {props.event.lon}
//   </p>
// </li>
// }
// </Col>

export default Event;


