//
//  PicAroundFirstViewController.m
//  PicAround
//
//  Created by BenA on 2/6/13.
//  Copyright (c) 2013 BenA. All rights reserved.
//

#import "PicAroundFirstViewController.h"
#import "EventCell.h"
#import <QuartzCore/QuartzCore.h>
#import "GalleryView.h"
@implementation PicAroundFirstViewController
@synthesize DidCreatedNewEvent;

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationController.navigationBar.translucent = NO;
    [self.navigationController.navigationBar setTintColor:[UIColor colorWithRed:45.0/255.0 green:150.0/255.0 blue:230.0/255.0 alpha:0]];
    DidCreatedNewEvent = NO;
    self.trackedViewName = @"Events Screen";
    id<GAITracker> tracker = [[GAI sharedInstance] defaultTracker];
    [tracker sendView:@"Events Screen"];
//    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc]
//										 
//										 initWithTarget:self action:@selector(handleTap)];
//	
//	singleTap.numberOfTapsRequired =1;
//	
//	[self.view addGestureRecognizer:singleTap];
	

    //[self.navigationController.navigationBar setAlpha:0.8];
    
    [self loadEvents];
    
    UIRefreshControl *refreshControl = [[UIRefreshControl alloc]init];
    self.refreshControl = refreshControl;
    [refreshControl addTarget:self action:@selector(loadEvents)forControlEvents:UIControlEventValueChanged];
    [self.tableView addSubview:refreshControl];
    self.tableView.alwaysBounceVertical = YES;
//	
//    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
//    if (!appDelegate.session.isOpen) {
//        [self performSegueWithIdentifier:@"LogoutTransition" sender:self];
//
//    }

    // Dispose of any resources that can be recreated.
}
//- (void)handleTap {
//    
//}

- (IBAction)logout:(id)sender
{
    [self performSegueWithIdentifier:@"settingsTransition" sender:self];
}


-(void) viewDidAppear:(BOOL)animated
{
    self.navigationItem.title = @"Events Around You";
   
    if (DidCreatedNewEvent == YES) {
        DidCreatedNewEvent = NO;
        [self loadEvents];
    }
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

-(void) loadEvents
{
    self.navigationItem.title = @"Events Around You";
    // Do any additional setup after loading the view, typically from a nib.
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    
    eventsDic = [[NSMutableDictionary alloc] init];
    
    self.paApi = [[PicAroundApi alloc]init];
    self.spinner.hidesWhenStopped = YES;
    [self.spinner startAnimating];
    self.tableViewArray = nil;
    [self.tableView reloadData];
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    [self.paApi findEventByLocation:appDelegate.user.id lat: @"32.784820" lon: @"35.005520" radius:@"10000000" completionBlock:^(NSArray *events, NSError *error) {
    //[self.paApi findEventByLocation:appDelegate.user.id lat: appDelegate.Latitude lon: appDelegate.Longitude radius:@"1000" completionBlock:^(NSArray *events, NSError *error) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
            [self.spinner stopAnimating];
            [self.refreshControl endRefreshing];
            if(events && [events count] > 0)
            {
                self.tableViewArray = events;
                int i;
                int counter = [events count];
                for (i=0; i<counter; i++) {
                    NSString* indexPathRow = [NSString stringWithFormat:@"%i",i];
                    EventCell *cell;
                    NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"EventCell" owner:nil options:nil];
                    cell =  [nib objectAtIndex:0];
                    PAEventEntity* event= (PAEventEntity*)[self.tableViewArray objectAtIndex:i];
                    if(event.EventName != [NSNull null])
                    {
                        [[cell eventName] setText:event.EventName];
                    }
                    if(event.Thumbnail != nil)
                    {
                        [[cell imageView] setImage:event.Thumbnail];
                    }
                    if(event.LocationName != [NSNull null])
                    {
                        [[cell eventLocation] setText:event.LocationName];
                    }
                    [eventsDic setObject:cell forKey:indexPathRow];
                }

                [self.tableView reloadData];

            }
        });
    } imageLoadedEvent:^(NSString *imageIndex, UIImage *photoImage) {
        EventCell *cell = [eventsDic objectForKey:imageIndex];
        if(cell!=nil)
        {
            dispatch_async(dispatch_get_main_queue(), ^{
                //NSLog(@"Setting image at index %@", imageIndex);
                cell.imageView.image = nil;
                [cell.imageView setImage:photoImage];
            });
        }
    }];  
}


-(NSInteger) tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    NSInteger count;
    count = [self.tableViewArray count];
    
    return count;
}

//-(UITableViewCell*) tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    UITableViewCell *cell =[tableView dequeueReusableCellWithIdentifier:@"SimpleTableIdentifier"];
//    if(cell == nil)
//    {
//        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:@"SimpleTableIdentifier"];
//    }
//
//    return cell;
//}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    //    static NSString *CellIdentifier = @"MyEventCell";
    //    NSLog(@"index Path %i", indexPath.row);
    //    EventCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    NSString* indexPathRow = [NSString stringWithFormat:@"%i",indexPath.row];
    EventCell *cellAtindexPathRow = [eventsDic objectForKey:indexPathRow];
    return (EventCell*)[eventsDic objectForKey:indexPathRow];
    //    if (cellAtindexPathRow == nil)
    //    {
    //        NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"EventCell" owner:nil options:nil];
    //        cell =  [nib objectAtIndex:0];
    //
    //        PAEventEntity* event= (PAEventEntity*)[self.tableViewArray objectAtIndex:[indexPath row]];
    //
    //
    //        if(event.EventName != [NSNull null])
    //        {
    //            [[cell eventName] setText:event.EventName];
    //        }
    //
    //        if(event.Thumbnail != nil)
    //        {
    //            [[cell imageView] setImage:event.Thumbnail];
    //        }
    //
    //
    //        if(event.LocationName != [NSNull null])
    //        {
    //            [[cell eventLocation] setText:event.LocationName];
    //        }
    //        [eventsDic setObject:cell forKey:indexPathRow];
    //    }
    //    else
    //    {
    //        return (EventCell*)[eventsDic objectForKey:indexPathRow];
    //    }
    //    //        for(id currentObject in topLevelObjects)
    //    //        {
    //    //            if([currentObject isKindOfClass:[EventCell class]])
    //    //            {
    //    //                cell = (EventCell *)currentObject;
    //    //                break;
    //    //            }
    //    //        }
    //
    //
    //    return cell;
}
-(CGFloat) tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 69;
}
-(void) tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    PAEventEntity* event= (PAEventEntity*)[self.tableViewArray objectAtIndex:[indexPath row]];
    
    NSString* eventID = [NSString stringWithFormat:@"%@", event.EventId];
    NSString* eventName = [NSString stringWithFormat:@"%@", event.EventName];
    NSString* locationName = [NSString stringWithFormat:@"%@", event.LocationName];
    
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    appDelegate.EventID = eventID;
    appDelegate.EventName = eventName;
    appDelegate.SelectedLocationName = locationName;
    
    self.title = @"Events";
    self.navigationItem.title = @"Events";
    [self performSegueWithIdentifier:@"moveToGallery" sender:self];
    //GalleryView* gv = [[GalleryView alloc] initWithNibName:@"GalleryView" bundle:nil];
    //[self.navigationController pushViewController:gv animated:YES ];
    //[self.tabBarController setSelectedIndex:2];
    
    //UINavigationController * nc = [[UINavigationController alloc] initWithRootViewController:[gv ]];
    //[self presentModalViewController:nc animated:YES];
    
}

- (void)viewDidUnload {
    [self setTableView:nil];
    [self setSpinner:nil];
    //[self setLogout:nil];
    [super viewDidUnload];
}
- (IBAction)refresh:(id)sender {
    //[self loadEvents];
    //[self closeSession];
    //[self performSegueWithIdentifier:@"settginsTransition" sender:self];
    
}

- (IBAction)createNewEvent {
    [self performSegueWithIdentifier:@"newEventSegue" sender:self];
    
}

- (IBAction)loadMyEvents {
   
    self.navigationItem.title = @"Your Events";

    // Do any additional setup after loading the view, typically from a nib.
    PicAroundAppDelegate *appDelegate = [[UIApplication sharedApplication]delegate];
    
    eventsDic = [[NSMutableDictionary alloc] init];
    
    self.paApi = [[PicAroundApi alloc]init];
    self.spinner.hidesWhenStopped = YES;
    [self.spinner startAnimating];
    self.tableViewArray = nil;
    [self.tableView reloadData];
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    [self.paApi loadUserEvents:appDelegate.user.id completionBlock:^(NSArray *events, NSError *error) {
        // [self.paApi findEventByLocation:appDelegate.user.id lat: appDelegate.Latitude lon: appDelegate.Longitude radius:@"1000" completionBlock:^(NSArray *events, NSError *error) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
            [self.spinner stopAnimating];
            if(events && [events count] > 0)
            {
                self.tableViewArray = events;
                int i;
                int counter = [events count];
                for (i=0; i<counter; i++) {
                    NSString* indexPathRow = [NSString stringWithFormat:@"%i",i];
                    EventCell *cell;
                    NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"EventCell" owner:nil options:nil];
                    cell =  [nib objectAtIndex:0];
                    PAEventEntity* event= (PAEventEntity*)[self.tableViewArray objectAtIndex:i];
                    if(event.EventName != [NSNull null])
                    {
                        [[cell eventName] setText:event.EventName];
                    }
                    if(event.Thumbnail != nil)
                    {
                        [[cell imageView] setImage:event.Thumbnail];
                    }
                    if(event.LocationName != [NSNull null])
                    {
                        [[cell eventLocation] setText:event.LocationName];
                    }
                    [eventsDic setObject:cell forKey:indexPathRow];
                }
                
                [self.tableView reloadData];
                
            }
        });
    } imageLoadedEvent:^(NSString *imageIndex, UIImage *photoImage) {
        EventCell *cell = [eventsDic objectForKey:imageIndex];
        if(cell!=nil)
        {
            dispatch_async(dispatch_get_main_queue(), ^{
                //NSLog(@"Setting image at index %@", imageIndex);
                cell.imageView.image = nil;
                [cell.imageView setImage:photoImage];
            });
        }
    }];  

}
- (IBAction)menu:(id)sender
{
    sheet = [[UIActionSheet alloc] initWithTitle:@"Options"
                                        delegate:self
                               cancelButtonTitle:@"Cancel"
                          destructiveButtonTitle:nil
                               otherButtonTitles:@"Create New Event", @"Load My Events", @"Check For New Events",@"Send Feedback", nil];
    
    // Show the sheet
    [sheet showInView:[self.view window]];
}
-(void) actionSheet:(UIActionSheet*) actionSheet didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    if(buttonIndex == 0)//New Event
    {
        [self createNewEvent];
        
    }
    else if(buttonIndex == 1)//Load My Events
    {
        [self loadMyEvents];
    }
    else  if(buttonIndex == 2)//Check for new events
    {
        [self loadEvents];
    }
    else if(buttonIndex == 3)//Send Feedback
    {
        [TestFlight openFeedbackView];
    }

//    else  if(buttonIndex == 4)//Share this app
//    {
//        [self shareToSMS];
//    }
    else
    {
        
    }
    [sheet dismissWithClickedButtonIndex:buttonIndex animated:YES];
}
@end
