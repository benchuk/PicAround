//
//  PAImageDetailController.m
//  PicAround
//
//  Created by BenA on 2/11/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//


#import "PAImageDetailController.h"
#import "PicAroundAppDelegate.h"
#import "PicAroundApi.h"
#import <QuartzCore/QuartzCore.h>

@interface PAImageDetailController ()

@end

@implementation PAImageDetailController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (IBAction)openUserPage:(id)sender {
    NSString * userID = [NSString stringWithFormat:@"%@",self.PhotoEntity.UserId];
    NSString* url = [NSString stringWithFormat:@"http://facebook.com/%@",userID];
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    
    if (appDelegate.session.isOpen) {
        NSString * userID = [NSString stringWithFormat:@"%@",self.PhotoEntity.UserId];
        NSDictionary* params = [NSDictionary dictionaryWithObject:@"id,name" forKey:@"fields"];
        FBRequest* rq = [FBRequest requestWithGraphPath:userID  parameters:params HTTPMethod:nil];
        [rq startWithCompletionHandler:^(FBRequestConnection *connection, id result, NSError *error) {
            NSDictionary* dic = (NSDictionary *)result;
           
            dispatch_async(dispatch_get_main_queue(), ^{
                self.userName.text =[dic objectForKey:@"name"];
            });
        }];
        NSString* url = [NSString stringWithFormat:@"http://graph.facebook.com/%@/picture?type=large",userID];
        NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:url]
                                                  options:0
                                                    error:nil];
        UIImage *image = [UIImage imageWithData:imageData];
        if(self.PhotoEntity.LocationName != [NSNull null])
        {
            self.locationName.text = self.PhotoEntity.LocationName;
        }
        self.profileImage.image = image;

        self.profileImage.clipsToBounds = YES;
        self.profileImage.layer.cornerRadius = 10.0;
        self.profileImage.layer.masksToBounds = YES;
        self.profileImage.layer.borderColor = [UIColor blackColor].CGColor;
        self.profileImage.layer.borderWidth = 1.0;

        self.detailsBackground.clipsToBounds = YES;
        self.detailsBackground.layer.cornerRadius = 15.0;
        self.detailsBackground.layer.masksToBounds = YES;
    
    }
    
    appDelegate.DetailedViewSelected = YES;
    self.imageView.image = self.img;
   
    if(self.PhotoEntity.LargeImage == nil)
    {
        self.spinner.hidesWhenStopped = YES;
        [self.spinner startAnimating];
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_async(queue, ^{
        NSError *error2 = nil;
        
        NSString *linkToMidQImage = [PicAroundApi picAroundFullLinkURLForimageLink:self.PhotoEntity.MidQLink];
        NSData *imageDataMidQ = [NSData dataWithContentsOfURL:[NSURL URLWithString:linkToMidQImage]
                                                      options:0
                                                        error:&error2];
        
        
        dispatch_async(dispatch_get_main_queue(), ^{
            //[self.spinner stopAnimating];
            //self.spinner.hidesWhenStopped = YES;
            self.spinner.hidesWhenStopped = YES;
            [self.spinner stopAnimating];
            UIImage *imageMidQ = [UIImage imageWithData:imageDataMidQ];
            self.img = imageMidQ;
            self.imageView.image = imageMidQ;
            if (!appDelegate.session.isOpen) {
                [appDelegate sessionOpener];
            }
        });
        
    });
    }
    else
    {
        self.spinner.hidesWhenStopped = YES;
        [self.spinner stopAnimating];
        self.imageView.image = self.PhotoEntity.LargeImage;
        self.img = self.PhotoEntity.LargeImage;
        
    }
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)share:(id)sender
{
    sheet = [[UIActionSheet alloc] initWithTitle:@"Share"
                                        delegate:self
                               cancelButtonTitle:@"Cancel"
                          destructiveButtonTitle:nil
                               otherButtonTitles:@"Facebook", @"Twitter", @"Email", @"SMS", nil];
    
    // Show the sheet
    [sheet showInView:[self.view window]];
}



-(void) actionSheet:(UIActionSheet*) actionSheet didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 0)//facebook
    {
        [self postToFacebook];
        
    }
    else if(buttonIndex == 1)//Twitter
    {
        [self shareToTwitter];
    }
    else  if(buttonIndex == 2)//Email
    {
        [self shareToEmail];
    }
    else  if(buttonIndex == 3)//SMS
    {
        [self shareToSMS];
    }
    else
    {
        
    }
    [sheet dismissWithClickedButtonIndex:buttonIndex animated:YES];
}

-(void) shareToSMS
{
    MFMessageComposeViewController *controller = [[MFMessageComposeViewController alloc] init];
	if([MFMessageComposeViewController canSendText])
	{
        PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
        
        NSString* smsBody = [NSString stringWithFormat: @"Picaround has a picture to share with you \r\n http://picaround.azurewebsites.net/Gallery/GalleryByEventId?eventId=%@",appDelegate.EventID];
		controller.body = smsBody;
		//controller.recipients = [NSArray arrayWithObjects:@"12345678", @"87654321", nil];
		controller.messageComposeDelegate = self;
		[self presentModalViewController:controller animated:YES];
	}
}

- (void)messageComposeViewController:(MFMessageComposeViewController *)controller didFinishWithResult:(MessageComposeResult)result
{
	switch (result)
    {
		case MessageComposeResultCancelled:
			NSLog(@"Cancelled");
			break;
		case MessageComposeResultFailed:
        {
			UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"Failure" message:@"Your device doesn't support sharing via SMS"
                                                            delegate:nil cancelButtonTitle:@"OK" otherButtonTitles: nil];
            [alert show];
        }
			break;
		case MessageComposeResultSent:
			break;
		default:
			break;
	}
    
	[self dismissModalViewControllerAnimated:YES];
}

-(void) shareToTwitter
{
    if([SLComposeViewController isAvailableForServiceType:SLServiceTypeTwitter])
    {
        PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
        
        slComposeViewController = [SLComposeViewController composeViewControllerForServiceType:SLServiceTypeTwitter];
        [slComposeViewController addImage:self.img];
        NSString* url = [NSString stringWithFormat: @"http://picaround.azurewebsites.net/Gallery/GalleryByEventId?eventId=%@",appDelegate.EventID];
        [slComposeViewController addURL:[NSURL URLWithString:url]];
        [slComposeViewController setInitialText:@"Check out PicAround"];
        [self presentViewController:slComposeViewController animated:YES completion:NULL];
    }
}

-(void) shareToEmail
{
    if ([MFMailComposeViewController canSendMail])
    {
        MFMailComposeViewController *mailer = [[MFMailComposeViewController alloc] init];
        mailer.mailComposeDelegate = self;
        [mailer setSubject:@"PicAround has something to share with you."];
        //NSArray *toRecipients = [NSArray arrayWithObjects:@"fisrtMail@example.com", @"secondMail@example.com", nil];
        //[mailer setToRecipients:toRecipients];
        //UIImage *myImage = [UIImage imageNamed:@"mobiletuts-logo.png"];
        NSData *imageData = UIImagePNGRepresentation(self.img);
        [mailer addAttachmentData:imageData mimeType:@"image/png" fileName:@"picaroundImage"];
        PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
        
        NSString* emailBody = [NSString stringWithFormat: @"Picaround has a picture to share with you \r\n http://picaround.azurewebsites.net/Gallery/GalleryByEventId?eventId=%@",appDelegate.EventID];
        [mailer setMessageBody:emailBody isHTML:NO];
        [self presentModalViewController:mailer animated:YES];
    }
    else
    {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Failure"
                                                        message:@"Your device doesn't support sharing via email"
                                                       delegate:nil
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles: nil];
        [alert show];
    }
}

- (void)mailComposeController:(MFMailComposeViewController*)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError*)error
{
    switch (result)
    {
        case MFMailComposeResultCancelled:
            NSLog(@"Mail cancelled: you cancelled the operation and no email message was queued.");
            break;
        case MFMailComposeResultSaved:
            NSLog(@"Mail saved: you saved the email message in the drafts folder.");
            break;
        case MFMailComposeResultSent:
            NSLog(@"Mail send: the email message is queued in the outbox. It is ready to send.");
            break;
        case MFMailComposeResultFailed:
            NSLog(@"Mail failed: the email message was not saved or queued, possibly due to an error.");
            break;
        default:
            NSLog(@"Mail not sent.");
            break;
    }
    // Remove the mail view
    [self dismissModalViewControllerAnimated:YES];
}


-(void) postToFacebook
{
    if([SLComposeViewController isAvailableForServiceType:SLServiceTypeFacebook])
    {
        PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
        
        slComposeViewController = [SLComposeViewController composeViewControllerForServiceType:SLServiceTypeFacebook];
        [slComposeViewController addImage:self.img];
        NSString* url = [NSString stringWithFormat: @"http://picaround.azurewebsites.net/Gallery/GalleryByEventId?eventId=%@",appDelegate.EventID];
        [slComposeViewController addURL:[NSURL URLWithString:url]];
        [slComposeViewController setInitialText:@"Check out PicAround"];
        [self presentViewController:slComposeViewController animated:YES completion:NULL];
    }
}
- (void)viewDidUnload {
    [self setSpinner:nil];
    [self setProfileImage:nil];
    [self setUserName:nil];
    [self setProfileImage:nil];
    [self setProfileImage:nil];
    [self setDetailsBackground:nil];
    [self setLocationName:nil];
    [super viewDidUnload];
}
@end
