import React from 'react';
import { getEvents } from '../actions/events';
import { connect } from 'react-redux';

import Event from './Event'

const Wall = props => {
    const eventsAsObj = props.events
    const eventsKeys = Object.keys(eventsAsObj)
    const events = eventsKeys.map(key => Object.assign(eventsAsObj[key], {id: key}))
    console.log('events in component: ', events.map(event => event.lat))
    return (
      <ul className="list-group event-list list-unstyled">
          {
              events.map(event => (<Event key={event.id} event={event}></Event>))
          }
      </ul>
    )
}


function mapStateToProps(state) {
    return {
        events: state.events,
    }
}
const WallContainer = connect(mapStateToProps, null)(Wall);

export default WallContainer;


