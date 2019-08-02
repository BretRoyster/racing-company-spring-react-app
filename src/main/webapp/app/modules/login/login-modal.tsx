import React from 'react';

import './login-modal.scss';

import { Button, Modal, ModalHeader, ModalBody, ModalFooter, Label, Alert, Row, Col } from 'reactstrap';
import { AvForm, AvField, AvGroup, AvInput } from 'availity-reactstrap-validation';
import { Link } from 'react-router-dom';

export interface ILoginModalProps {
  showModal: boolean;
  loginError: boolean;
  handleLogin: Function;
  handleClose: Function;
}

class LoginModal extends React.Component<ILoginModalProps> {
  handleSubmit = (event, errors, { username, password, rememberMe }) => {
    const { handleLogin } = this.props;
    handleLogin(username, password, rememberMe);
  };

  render() {
    const { loginError, handleClose } = this.props;

    return (
      // <Modal isOpen={this.props.showModal} toggle={handleClose} backdrop="static" id="login-page" autoFocus={false}>
      <AvForm onSubmit={this.handleSubmit}>
        <div id="login-title" style={{ display: 'flex', justifyContent: 'center' }}>
          {
            // toggle={handleClose}
          }
          <img src="content/images/racing_logo.jpg" style={{ maxWidth: 300 }} alt="Logo" />
        </div>
        <div className="login-box">
          <div style={{ maxWidth: 450 }}>
            <Row>
              <Col md="12">
                {loginError ? (
                  <Alert color="danger">
                    <strong>Failed to sign in!</strong> Please check your credentials and try again.
                  </Alert>
                ) : null}
              </Col>
              <Col md="12">
                <AvField
                  name="username"
                  // label="Username"
                  placeholder="Your username"
                  required
                  errorMessage="Username cannot be empty!"
                  autoFocus
                />
                <AvField
                  name="password"
                  type="password"
                  // label="Password"
                  placeholder="Your password"
                  required
                  errorMessage="Password cannot be empty!"
                />
                <AvGroup check inline>
                  <Label className="form-check-label">
                    <AvInput type="checkbox" name="rememberMe" /> Remember me
                  </Label>
                </AvGroup>
              </Col>
            </Row>
            <div className="mt-1">&nbsp;</div>
            {
              // <Alert color="warning">
              //   <Link to="/reset/request">Did you forget your password?</Link>
              // </Alert>
              // <Alert color="warning">
              //   <span>You don't have an account yet?</span> <Link to="/register">Register a new account</Link>
              // </Alert>
            }
          </div>
          <div>
            {
              // <Button color="secondary" onClick={handleClose} tabIndex="1">
              //   Cancel
              // </Button>{' '}
            }
            <Button color="primary" type="submit" style={{ width: 175 }}>
              Log in
            </Button>
          </div>
        </div>
        <div style={{ display: 'flex', justifyContent: 'center', paddingTop: 16 }}>
          <span style={{ display: 'block' }}>
            Forgot your password? <Link to="/reset/request">Reset Password</Link>
          </span>
        </div>
      </AvForm>
      // </Modal>
    );
  }
}

export default LoginModal;
