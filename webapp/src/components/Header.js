import React from 'react';
import { getEvents, getLocations } from '../actions/events';
import { connect } from 'react-redux';

class Header extends React.Component {

    componentWillMount() {
        this.props.dispatchGetEvents();
        this.props.dispatchGetLocations();
    }

    render() {
        return (<span>Header is getting events</span>);
    }
}

function mapDispatchToProps(dispatch) {
    return {
        dispatchGetEvents: () => dispatch(getEvents()),
        dispatchGetLocations: () => dispatch(getLocations())
    };
}

const HeaderContainer = connect(null, mapDispatchToProps)(Header);

export default HeaderContainer;

