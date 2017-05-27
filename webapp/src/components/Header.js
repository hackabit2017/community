import React from 'react';
import { getEvents } from '../actions/events';
import { connect } from 'react-redux';

class Header extends React.Component {

    componentWillMount() {
        this.props.onGetEvent();
    }

    render() {
        return (<span>Header is getting events</span>);
    }
}

function mapDispatchToProps(dispatch) {
    return {
        onGetEvent: () => dispatch(getEvents()),
    };
}

const HeaderContainer = connect(null, mapDispatchToProps)(Header);

export default HeaderContainer;

