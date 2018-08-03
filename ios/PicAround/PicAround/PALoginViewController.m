//
//  PALoginViewController.m
//  PicAround
//
//  Created by BenA on 2/7/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//


#import "PicAroundAppDelegate.h"
#import "PALoginViewController.h"

@interface PALoginViewController ()

@end

@implementation PALoginViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	self.title = @"";
    [self.navigationController setNavigationBarHidden:YES animated:NO];
     self.trackedViewName = @"Login Screen";
    id<GAITracker> tracker = [[GAI sharedInstance] defaultTracker];
    [tracker sendView:@"Login Screen"];
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
//    if (appDelegate.session.state != FBSessionStateCreated && appDelegate.session.state !=FBSessionStateClosed) {
//        appDelegate.session = [[FBSession alloc] init];
//        [FBSession setActiveSession:appDelegate.session];
//    }
//     if (!appDelegate.session.isOpen) {
//         appDelegate.session = [[FBSession alloc] init];
//     
//            //if (appDelegate.session.state == FBSessionStateCreatedTokenLoaded) {
//                // even though we had a cached token, we need to login to make the session usable
//                [appDelegate.session openWithCompletionHandler:^(FBSession *session,
//                                                                 FBSessionState status,
//                                                                 NSError *error) {
//                    // we recurse here, in order to update buttons and labels
//                    [FBSession setActiveSession:appDelegate.session];
//                    [[FBRequest requestForMe] startWithCompletionHandler:
//                     ^(FBRequestConnection *connection, NSDictionary<FBGraphUser> *user, NSError *error) {
//                         if (!error) {
//                            appDelegate.user = user;
//                         }
//                     }];
//                    
//                }];
//     }
           // }

    if (FBSession.activeSession.isOpen) {
        _authButton.hidden = YES;
        _loginMessage.hidden = YES;
//        PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
//        [appDelegate.session openWithCompletionHandler:^(FBSession *session,
//                                                         FBSessionState status,
//                                                         NSError *error) {
//            //[FBSession setActiveSession:appDelegate.session];
//            [[FBRequest requestForMe] startWithCompletionHandler:
//             ^(FBRequestConnection *connection, NSDictionary<FBGraphUser> *user, NSError *error) {
//                 if (!error) {
//                     appDelegate.user = user;
//                     [self performSelector:@selector(transition) withObject:nil afterDelay:3];                                    }
//             }];
//             // and here we make sure to update our UX according to the new session state
//            
//            
//        }];
        [self performSelector:@selector(transition) withObject:nil afterDelay:3];
    }
}
-(void) transition
{
    [self performSegueWithIdentifier:@"LoginTransition" sender:self];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)authButtonAction:(id)sender {
    
    id<GAITracker> tracker = [[GAI sharedInstance] defaultTracker];
    [tracker sendEventWithCategory:@"uiAction"
                        withAction:@"buttonPress"
                         withLabel:@"Facebook Login IOS"
                         withValue:[NSNumber numberWithInt:100]];
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication] delegate];
    
    // If the person is authenticated, log out when the button is clicked.
    // If the person is not authenticated, log in when the button is clicked.
           // The person has initiated a login, so call the openSession method
        // and show the login UX if necessary.
     //[appDelegate openSessionWithAllowLoginUI:YES];
    
    
    if (appDelegate.session.state != FBSessionStateCreated) {
        // Create a new, logged out session.
        appDelegate.session = [[FBSession alloc] init];
    }
  
    // if the session isn't open, let's open it now and present the login UX to the user
    [appDelegate.session openWithCompletionHandler:^(FBSession *session,
                                                     FBSessionState status,
                                                     NSError *error) {
        appDelegate.session = session;
        [FBSession setActiveSession:appDelegate.session];
        [[FBRequest requestForMe] startWithCompletionHandler:
         ^(FBRequestConnection *connection, NSDictionary<FBGraphUser> *user, NSError *error) {
             if (!error) {
                 appDelegate.user = user;
                  NSString* userIDMsg = [NSString stringWithFormat:@"User %@ Logged In",user.id];
                 [TestFlight passCheckpoint:userIDMsg];
                 _authButton.hidden = YES;
                 [self transition];
             }
         }];   // and here we make sure to update our UX according to the new session state
       
    }];
    // The user has initiated a login, so call the openSession method
    // and show the login UX if necessary.
    //[appDelegate openSessionWithAllowLoginUI:YES];
}
- (void)viewDidUnload {
    [self setAuthButton:nil];
    [self setLoginMessage:nil];
    [super viewDidUnload];
}
@end
