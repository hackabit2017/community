import React from 'react';
import { connect } from 'react-redux';

import Event from './Event'

const _sortByDate = (event1, event2) => {
    const date1 = event1.date
    const date2 = event2.date
    if (date1 < date2) {
        return 1
    }
    else if (date1 > date2) {
        return -1
    }
    else {
        return 0
    }
}

const Wall = props => {
    const eventsAsObj = props.events
    const eventsKeys = Object.keys(eventsAsObj)
    const events = eventsKeys
      .map(key => Object.assign(eventsAsObj[key], {id: key}))
      .sort(_sortByDate)
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


