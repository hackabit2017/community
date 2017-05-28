import React from 'react';
import ReactMapboxGl, { Layer, Feature, ZoomControl } from "react-mapbox-gl";
import { connect } from 'react-redux';

const EventsMap = (props) => {
  const eventsAsObj = props.events
  const eventsKeys = Object.keys(eventsAsObj)
  const events = eventsKeys.map(key => Object.assign(eventsAsObj[key], {id: key}))

  const _onToggleHover = (pointer, mouseEvent) =>  {
    mouseEvent.map.getCanvas().style.cursor = pointer;
  }

  const _markerClick = (event, mouseEvent) => {
    console.log('You have clicked on: ', event)
  }

  return (
    <div className="events-map">
      <ReactMapboxGl
        style="mapbox://styles/mapbox/streets-v8"
        accessToken="pk.eyJ1Ijoidm1hZGluY2VhIiwiYSI6ImNqMzd5NDllcjAwMnoyd285bHJodjF5emYifQ.aogNUPgaPnhLZB91IERbBw"
        containerStyle={{
                    height: "80vh",
                  }}
        center={[ 21.226889, 45.755446 ]}
        zoom={[12]}
        pins={[ 21.226889, 45.755446 ]}
      >
        <ZoomControl
          position="bottomLeft"
        />
        <Layer
          type="symbol"
          id="marker"
          layout={{ "icon-image": "marker-15" , 'icon-size': 1.5}}>
          {
            events.map(event => (
              <Feature
                key={event.id}
                coordinates={[event.lon, event.lat]}
                onMouseEnter={_onToggleHover.bind(this, "pointer")}
                onMouseLeave={_onToggleHover.bind(this, "")}
                onClick={_markerClick.bind(this, event)}
                />
            ))
          }
        </Layer>
      </ReactMapboxGl>
    </div>
  )
}

function mapStateToProps(state) {
  return {
    events: state.events,
  }
}
const EventsMapContainer = connect(mapStateToProps, null)(EventsMap);

export default EventsMapContainer

//<Feature coordinates={[21.226889, 45.755446]}/>
//<Feature coordinates={[21.226881, 45.755442]}/>

