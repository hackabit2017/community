import React from 'react';
import ReactMapboxGl, { Layer, Feature } from "react-mapbox-gl";

const EventsMap = (props) => {
  return (
    <div className="events-map">
      <ReactMapboxGl
        style="mapbox://styles/mapbox/streets-v9"
        accessToken="pk.eyJ1Ijoidm1hZGluY2VhIiwiYSI6ImNqMzd5NDllcjAwMnoyd285bHJodjF5emYifQ.aogNUPgaPnhLZB91IERbBw"
        containerStyle={{
                    height: "80vh",
                  }}
        center={[45.75, 21.22]}
      >
        <Layer
          type="symbol"
          id="marker"
          layout={{ "icon-image": "marker-15" }}>
          <Feature coordinates={[45.7524606, 21.2231719]}/>
        </Layer>
      </ReactMapboxGl>
    </div>
  )
}

export default EventsMap